package in.ac.iiitb.plproject.mutation;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Runs the mutants of ONE Defects4J class against every tool's test suites.
 *
 * <pre>
 *   D4jMutationRun &lt;run.properties&gt;
 * </pre>
 *
 * Steps (all with the same rules for every tool):
 * <ol>
 *   <li>Compile the unmutated class alone ({@code orig}), and the adapter, against
 *       the project's compiled classes. Every test run puts {@code orig} or one
 *       mutant's build first on the classpath, so the only difference between the
 *       baseline run and a mutant run is the mutated class.</li>
 *   <li>Compile every suite against that build.</li>
 *   <li>Baseline: run every suite twice on the correct build. A test class that runs
 *       no test (all its tests were removed by Defects4J's cleanup) is left out of
 *       its suite; a suite with any failure, or no test class left, is dropped and
 *       counted, never scored.</li>
 *   <li>Plant each mutant at its line, compile only the mutated file. No compile =
 *       INVALID; class files byte-identical to the original = TCE (trivially
 *       equivalent, by the compiler). Both are left out for every tool.</li>
 *   <li>Draw a seeded random sample of {@code cap} mutants from the rest.</li>
 *   <li>Run every (sampled mutant, kept suite) pair in its own child JVM
 *       ({@link MutantTestMain}) with the same wall-clock limit. Any FAIL, crash or
 *       timeout = KILLED. One row per pair; the run resumes from existing rows.</li>
 * </ol>
 */
public final class D4jMutationRun {

    private final Properties p;
    private final String key;
    private final Path target;
    private final Path work;
    private final Path out;
    private final String projectClasses;
    private final String runtimeCp;      // junit, hamcrest, evosuite runtime
    private final String mutationCp;     // MutantTestMain
    private final int threads;
    private final int timeoutSeconds;
    private final String javaBin;
    private final String relPath;        // org/apache/.../SetUniqueList.java

    public static void main(String[] args) throws Exception {
        Properties p = new Properties();
        try (FileInputStream in = new FileInputStream(args[0])) { p.load(in); }
        new D4jMutationRun(p).run();
    }

    private D4jMutationRun(Properties p) throws IOException {
        this.p = p;
        key = p.getProperty("key");
        target = Paths.get(p.getProperty("target"));
        work = Paths.get(p.getProperty("work"));
        out = Paths.get(p.getProperty("out"));
        projectClasses = p.getProperty("projectClasses");
        runtimeCp = p.getProperty("runtimeCp");
        mutationCp = p.getProperty("mutationCp");
        threads = Integer.parseInt(p.getProperty("threads", "8"));
        timeoutSeconds = Integer.parseInt(p.getProperty("timeoutSeconds", "60"));
        javaBin = System.getProperty("java.home") + "/bin/java";
        String src = new String(Files.readAllBytes(target), StandardCharsets.UTF_8);
        java.util.regex.Matcher pkg = java.util.regex.Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;").matcher(src);
        relPath = (pkg.find() ? pkg.group(1).replace('.', '/') + "/" : "") + target.getFileName();
        Files.createDirectories(work);
        Files.createDirectories(out);
    }

    // ── suites ──

    static final class Suite {
        final String tool, name;
        final List<String> sources = new ArrayList<String>();
        final List<String> classes = new ArrayList<String>();
        Path classDir;
        String status = "KEPT", reason = "";
        long[] baselineMillis = new long[2];
        Suite(String tool, String name) { this.tool = tool; this.name = name; }
    }

    private void run() throws Exception {
        // 1. orig + adapter
        Path origDir = work.resolve("orig");
        Path adapterDir = work.resolve("adapter");
        deleteRecursively(origDir);
        deleteRecursively(adapterDir);
        String err = javac(origDir, projectClasses, Collections.singletonList(target.toString()));
        if (err != null) throw new IllegalStateException("the unmutated class does not compile:\n" + err);
        err = javac(adapterDir, origDir + ":" + projectClasses + ":" + runtimeCp,
                Collections.singletonList(p.getProperty("adapter")));
        if (err != null) throw new IllegalStateException("the adapter does not compile:\n" + err);
        String libCp = adapterDir + ":" + projectClasses;   // after the class build

        // 2. suites
        List<Suite> suites = readSuites();
        for (Suite s : suites) {
            s.classDir = work.resolve("suites").resolve(s.name);
            deleteRecursively(s.classDir);
            err = javac(s.classDir, origDir + ":" + libCp + ":" + runtimeCp, s.sources);
            if (err != null) {
                s.status = "DROPPED";
                s.reason = "does not compile against the correct version: " + firstLine(err);
                continue;
            }
            for (String f : s.sources) {
                String text = new String(Files.readAllBytes(Paths.get(f)), StandardCharsets.UTF_8);
                if (!text.contains("@Test")) continue;   // scaffolding, suite aggregators
                java.util.regex.Matcher pk = java.util.regex.Pattern.compile("(?m)^\\s*package\\s+([\\w.]+)\\s*;").matcher(text);
                String simple = Paths.get(f).getFileName().toString().replaceFirst("\\.java$", "");
                s.classes.add((pk.find() ? pk.group(1) + "." : "") + simple);
            }
            Collections.sort(s.classes);
        }

        // 3. baseline, twice
        StringBuilder base = new StringBuilder("tool\tsuite\tstatus\ttest_classes\tms_run1\tms_run2\treason\n");
        for (Suite s : suites) {
            if (!s.status.equals("KEPT")) continue;
            List<String> empty = new ArrayList<String>();
            for (int round = 0; round < 2 && s.status.equals("KEPT"); round++) {
                long t0 = System.nanoTime();
                RunResult r = runSuite(origDir, libCp, s, s.classes, false);
                s.baselineMillis[round] = (System.nanoTime() - t0) / 1_000_000;
                for (Map.Entry<String, String[]> e : r.perClass.entrySet()) {
                    String[] v = e.getValue();
                    if (v[0].equals("PASS")) continue;
                    if (v.length > 2 && v[2].equals("no test ran")) { empty.add(e.getKey()); continue; }
                    s.status = "DROPPED";
                    s.reason = "fails on the correct version (run " + (round + 1) + "): " + e.getKey() + " "
                            + String.join(" ", Arrays.copyOfRange(v, 1, v.length));
                    break;
                }
                if (s.status.equals("KEPT") && r.timedOut) {
                    s.status = "DROPPED"; s.reason = "times out on the correct version";
                }
                if (s.status.equals("KEPT") && r.missing > 0) {
                    s.status = "DROPPED"; s.reason = "the test JVM ended without reporting " + r.missing + " class(es)";
                }
                if (round == 0) {
                    s.classes.removeAll(empty);
                    if (s.classes.isEmpty() && s.status.equals("KEPT")) {
                        s.status = "DROPPED"; s.reason = "no test left (every test class runs no test)";
                    }
                }
            }
            if (!empty.isEmpty() && s.status.equals("KEPT")) s.reason = "left out (no test): " + new HashSet<String>(empty);
        }
        for (Suite s : suites) {
            base.append(s.tool).append('\t').append(s.name).append('\t').append(s.status).append('\t')
                .append(s.classes.size()).append('\t').append(s.baselineMillis[0]).append('\t')
                .append(s.baselineMillis[1]).append('\t').append(s.reason).append('\n');
        }
        Files.write(out.resolve(key + ".baseline.tsv"), base.toString().getBytes(StandardCharsets.UTF_8));
        System.out.print(base);
        List<Suite> kept = suites.stream().filter(s -> s.status.equals("KEPT")).collect(Collectors.toList());

        // 4. plant + compile every mutant, TCE check
        List<String[]> mutants = readTsv(Paths.get(p.getProperty("mutants")));
        List<String> origLines = Arrays.asList(new String(Files.readAllBytes(target), StandardCharsets.UTF_8)
                .split("\n", -1));
        Map<String, String> status = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        Map<String, String> detail = Collections.synchronizedMap(new LinkedHashMap<String, String>());
        for (String[] m : mutants) status.put(m[0], "?");
        ExecutorService pool = Executors.newFixedThreadPool(threads);
        List<Future<?>> fs = new ArrayList<Future<?>>();
        AtomicInteger done = new AtomicInteger();
        for (String[] m : mutants) {
            fs.add(pool.submit(() -> {
                String id = m[0];
                try {
                    int line = Integer.parseInt(m[1]) - 1;
                    String find = m[6], replace = m.length > 7 ? m[7] : "";
                    if (!origLines.get(line).trim().equals(find)) {
                        status.put(id, "NOT_APPLICABLE");
                        detail.put(id, "line " + (line + 1) + " is not `" + find + "`");
                        return null;
                    }
                    List<String> lines = new ArrayList<String>(origLines);
                    String orig = lines.get(line);
                    String indent = orig.substring(0, orig.indexOf(orig.trim()));
                    lines.set(line, replace.isEmpty()
                            ? indent + "// " + find + "   // MUTANT " + id + ": statement deleted"
                            : indent + replace + "   // MUTANT " + id);
                    Path dir = work.resolve("mutants").resolve(id);
                    deleteRecursively(dir);
                    Path src = dir.resolve("src").resolve(relPath);
                    Files.createDirectories(src.getParent());
                    Files.write(src, String.join("\n", lines).getBytes(StandardCharsets.UTF_8));
                    String e = javac(dir.resolve("classes"), projectClasses, Collections.singletonList(src.toString()));
                    if (e != null) {
                        status.put(id, "INVALID");
                        detail.put(id, firstError(e));
                    } else if (sameClasses(origDir, dir.resolve("classes"))) {
                        status.put(id, "TCE");
                        detail.put(id, "byte-identical class files");
                    } else {
                        status.put(id, "VALID");
                    }
                } catch (Exception e) {
                    status.put(id, "ERROR");
                    detail.put(id, e.toString());
                }
                int n = done.incrementAndGet();
                if (n % 50 == 0) System.out.println("  compiled " + n + "/" + mutants.size());
                return null;
            }));
        }
        for (Future<?> f : fs) f.get();

        // 5. seeded sample of the valid ones
        int cap = Integer.parseInt(p.getProperty("cap", "300"));
        long seed = Long.parseLong(p.getProperty("seed", "1"));
        List<String> valid = new ArrayList<String>();
        for (String[] m : mutants) if (status.get(m[0]).equals("VALID")) valid.add(m[0]);
        List<String> sample = new ArrayList<String>(valid);
        if (sample.size() > cap) {
            Collections.shuffle(sample, new Random(seed));
            sample = new ArrayList<String>(sample.subList(0, cap));
        }
        Set<String> sampled = new HashSet<String>(sample);
        StringBuilder st = new StringBuilder("id\tstatus\tsampled\tdetail\n");
        for (String[] m : mutants) {
            st.append(m[0]).append('\t').append(status.get(m[0])).append('\t')
              .append(sampled.contains(m[0]) ? "yes" : "no").append('\t')
              .append(detail.getOrDefault(m[0], "").replace('\t', ' ').replace('\n', ' ')).append('\n');
        }
        Files.write(out.resolve(key + ".status.tsv"), st.toString().getBytes(StandardCharsets.UTF_8));
        Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
        for (String s : status.values()) counts.merge(s, 1, Integer::sum);
        System.out.println("mutants: " + counts + ", sampled " + sample.size() + " (cap " + cap + ", seed " + seed + ")");

        // 6. one child JVM per (mutant, suite); resume from existing rows
        Path rowsFile = out.resolve(key + ".rows.tsv");
        Set<String> doneRows = new HashSet<String>();
        if (Files.exists(rowsFile)) {
            for (String l : Files.readAllLines(rowsFile, StandardCharsets.UTF_8)) {
                String[] f = l.split("\t", -1);
                if (f.length > 6 && !f[0].equals("id") && f[6].equals(String.valueOf(timeoutSeconds))) {
                    doneRows.add(f[0] + "\t" + f[1]);
                }
            }
        } else {
            Files.write(rowsFile, "id\tsuite\ttool\toutcome\tkind\tms\ttimeout_s\tmessage\n".getBytes(StandardCharsets.UTF_8));
        }
        List<String[]> jobs = new ArrayList<String[]>();
        for (String id : sample) for (Suite s : kept) {
            if (!doneRows.contains(id + "\t" + s.name)) jobs.add(new String[] { id, s.name });
        }
        System.out.println("rows to run: " + jobs.size() + " (" + doneRows.size() + " already done), timeout "
                + timeoutSeconds + " s, " + threads + " in parallel");
        Map<String, Suite> byName = new LinkedHashMap<String, Suite>();
        for (Suite s : kept) byName.put(s.name, s);
        AtomicInteger ran = new AtomicInteger();
        long t0 = System.nanoTime();
        try (Writer rows = Files.newBufferedWriter(rowsFile, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            fs.clear();
            for (String[] job : jobs) {
                fs.add(pool.submit(() -> {
                    Suite s = byName.get(job[1]);
                    long start = System.nanoTime();
                    RunResult r = runSuite(work.resolve("mutants").resolve(job[0]).resolve("classes"), libCp, s,
                            s.classes, true);
                    long ms = (System.nanoTime() - start) / 1_000_000;
                    String outcome, kind, msg;
                    if (r.timedOut) { outcome = "KILLED"; kind = "TIMEOUT"; msg = "test JVM killed after " + timeoutSeconds + " s"; }
                    else if (r.firstFail != null) { outcome = "KILLED"; kind = r.firstFail[1]; msg = r.firstFail[0] + ": " + (r.firstFail.length > 2 ? r.firstFail[2] : ""); }
                    else if (r.missing > 0) { outcome = "KILLED"; kind = "CRASH"; msg = "test JVM ended without reporting " + r.missing + " class(es)"; }
                    else { outcome = "SURVIVED"; kind = ""; msg = ""; }
                    String row = job[0] + "\t" + s.name + "\t" + s.tool + "\t" + outcome + "\t" + kind + "\t" + ms
                            + "\t" + timeoutSeconds + "\t" + clip(msg) + "\n";
                    synchronized (rows) { rows.write(row); rows.flush(); }
                    int n = ran.incrementAndGet();
                    if (n % 200 == 0 || n == jobs.size()) {
                        long el = (System.nanoTime() - t0) / 1_000_000_000L;
                        System.out.println("  rows " + n + "/" + jobs.size() + "  (" + el + " s)");
                    }
                    return null;
                }));
            }
            for (Future<?> f : fs) f.get();
        }
        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.MINUTES);
    }

    // ── child JVM ──

    static final class RunResult {
        final Map<String, String[]> perClass = new LinkedHashMap<String, String[]>();  // PASS | FAIL,kind,msg
        boolean timedOut;
        int missing;
        String[] firstFail;   // class, kind, message
    }

    private RunResult runSuite(Path classBuild, String libCp, Suite s, List<String> classes, boolean untilFail)
            throws IOException, InterruptedException {
        List<String> cmd = new ArrayList<String>(Arrays.asList(javaBin, "-Xmx512m", "-XX:+UseSerialGC",
                "-XX:TieredStopAtLevel=1", "-Djava.awt.headless=true", "-cp",
                classBuild + ":" + libCp + ":" + s.classDir + ":" + runtimeCp + ":" + mutationCp,
                MutantTestMain.class.getName()));
        if (untilFail) cmd.add("--until-fail");
        cmd.addAll(classes);
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.environment().put("TZ", "America/Los_Angeles");
        pb.directory(work.toFile());
        pb.redirectErrorStream(true);
        Process proc = pb.start();
        List<String> lines = Collections.synchronizedList(new ArrayList<String>());
        Thread reader = new Thread(() -> {
            try (BufferedReader r = new BufferedReader(new InputStreamReader(proc.getInputStream(), StandardCharsets.UTF_8))) {
                String l;
                while ((l = r.readLine()) != null) if (l.startsWith("RESULT\t")) lines.add(l);
            } catch (IOException ignored) {
                // killed
            }
        });
        reader.start();
        boolean finished = proc.waitFor(timeoutSeconds, TimeUnit.SECONDS);
        if (!finished) {
            proc.descendants().forEach(ProcessHandle::destroyForcibly);
            proc.destroyForcibly();
            proc.waitFor(10, TimeUnit.SECONDS);
        }
        reader.join(5000);
        RunResult r = new RunResult();
        r.timedOut = !finished;
        synchronized (lines) {
            for (String l : lines) {
                String[] f = l.split("\t", 5);
                if (f.length < 3) continue;
                if (f[2].equals("PASS")) r.perClass.put(f[1], new String[] { "PASS" });
                else {
                    String kind = f.length > 3 ? f[3] : "EXCEPTION", msg = f.length > 4 ? f[4] : "";
                    r.perClass.put(f[1], new String[] { "FAIL", kind, msg });
                    if (r.firstFail == null) r.firstFail = new String[] { f[1], kind, msg };
                }
            }
        }
        if (finished && r.firstFail == null) {
            for (String c : classes) if (!r.perClass.containsKey(c)) r.missing++;
        }
        return r;
    }

    // ── helpers ──

    private List<Suite> readSuites() throws IOException {
        List<Suite> out = new ArrayList<Suite>();
        Path f = Paths.get(p.getProperty("suites"));
        for (String l : Files.readAllLines(f, StandardCharsets.UTF_8)) {
            if (l.trim().isEmpty() || l.startsWith("#")) continue;
            String[] c = l.split("\t");
            Suite s = new Suite(c[0], c[1]);
            Path src = Paths.get(c[2]);
            if (Files.isDirectory(src)) {
                try (Stream<Path> w = Files.walk(src)) {
                    w.filter(x -> x.toString().endsWith(".java")).sorted().forEach(x -> s.sources.add(x.toString()));
                }
            } else {
                s.sources.add(src.toString());
            }
            if (s.sources.isEmpty()) { s.status = "DROPPED"; s.reason = "no source files"; }
            out.add(s);
        }
        return out;
    }

    private static List<String[]> readTsv(Path f) throws IOException {
        List<String[]> out = new ArrayList<String[]>();
        List<String> lines = Files.readAllLines(f, StandardCharsets.UTF_8);
        for (int i = 1; i < lines.size(); i++) if (!lines.get(i).isEmpty()) out.add(lines.get(i).split("\t", -1));
        return out;
    }

    private static boolean sameClasses(Path a, Path b) throws IOException {
        Map<String, byte[]> x = classFiles(a), y = classFiles(b);
        if (!x.keySet().equals(y.keySet())) return false;
        for (String k : x.keySet()) if (!Arrays.equals(x.get(k), y.get(k))) return false;
        return true;
    }

    private static Map<String, byte[]> classFiles(Path dir) throws IOException {
        Map<String, byte[]> m = new LinkedHashMap<String, byte[]>();
        try (Stream<Path> w = Files.walk(dir)) {
            for (Path f : w.filter(x -> x.toString().endsWith(".class")).collect(Collectors.toList())) {
                m.put(dir.relativize(f).toString(), Files.readAllBytes(f));
            }
        }
        return m;
    }

    private static String javac(Path outDir, String cp, List<String> sources) throws IOException {
        Files.createDirectories(outDir);
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
        List<String> args = new ArrayList<String>(Arrays.asList("-nowarn", "-Xlint:none", "-proc:none",
                "-implicit:none", "-encoding", "UTF-8", "-cp", cp, "-d", outDir.toString()));
        args.addAll(sources);
        ByteArrayOutputStream err = new ByteArrayOutputStream();
        int rc = compiler.run(null, null, err, args.toArray(new String[0]));
        return rc == 0 ? null : err.toString(StandardCharsets.UTF_8.name());
    }

    private static String firstError(String javacOutput) {
        for (String l : javacOutput.split("\n")) if (l.contains("error:")) return l.substring(l.indexOf("error:")).trim();
        return firstLine(javacOutput);
    }

    private static String firstLine(String s) {
        int i = s.indexOf('\n');
        return i < 0 ? s : s.substring(0, i);
    }

    private static String clip(String s) {
        s = s.replace('\t', ' ').replace('\n', ' ');
        return s.length() > 300 ? s.substring(0, 300) : s;
    }

    static void deleteRecursively(Path f) throws IOException {
        if (!Files.exists(f)) return;
        try (Stream<Path> w = Files.walk(f)) {
            for (Path x : w.sorted(Collections.reverseOrder()).collect(Collectors.toList())) Files.delete(x);
        }
    }

}
