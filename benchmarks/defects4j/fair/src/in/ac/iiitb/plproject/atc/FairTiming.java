package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * TestGen's running time, per test string (fair/README.md, "TestGen running time"). It only calls TestGen
 * ({@link GenatcMain#generate}) and changes nothing in it; it lives in TestGen's {@code atc} package only to
 * build a {@link LibraryRegistry.Entry} for a Defects4J adapter, as {@code runner/.../D4jMain} does.
 *
 * <p>Each string is generated ALONE, from its own one-string {@code .tests} file in the scratch folder, so its
 * time is its own. total_ms = wall clock of {@code GenatcMain.generate} (IR, every genCTC round with its SPF
 * run, printing the JUnit class; not compiling or running it), on the monotonic clock; wall_ms = the same on the
 * system clock, which TestGen's own timer uses (a difference shows the system clock was stepped, as WSL2 does).
 * spf_ms = TestGen's own SPF time
 * ({@code SequenceOutcome.spfMillis}, the engine time of every round). rounds = genCTC rounds that turned inputs
 * concrete; spf_runs = symex calls.
 *
 * <pre>
 *   FairTiming d4j &lt;scope&gt; &lt;spec&gt; &lt;tests&gt; &lt;fixed-sources.txt&gt; &lt;scratch&gt; &lt;run&gt;     (cwd: benchmarks/defects4j)
 *   FairTiming lib &lt;key&gt; &lt;scratch&gt; &lt;run&gt; &lt;len&gt;...                                (cwd: pl-platform-testing)
 * </pre>
 * The lib mode takes the (one) test string in the library's registered .tests file, writes its prefixes of the
 * given lengths to temporary .tests files under the scratch folder and runs each through
 * {@link LibraryRegistry#variantOf}. Prints one TSV row per string (no header).
 */
public final class FairTiming {

    public static void main(String[] args) throws Exception {
        if (args[0].equals("d4j")) {
            String scope = args[1], spec = args[2], tests = args[3];
            List<String> sources = new ArrayList<String>();
            for (String l : Files.readAllLines(Paths.get(args[4]), StandardCharsets.UTF_8)) if (!l.trim().isEmpty()) sources.add(l.trim());
            Path scratch = Paths.get(args[5]);
            String run = args[6];
            List<TestStringAst> all = TestStringParser.parseAll(read(tests), tests);
            for (TestStringAst t : all) {
                Path dir = scratch.resolve(scope).resolve("run" + run).resolve(t.getName());
                Path one = dir.resolve("one.tests");
                Files.createDirectories(dir);
                Files.write(one, testText(t.getName(), t.getCalls()).getBytes(StandardCharsets.UTF_8));
                LibraryRegistry.Entry e = new LibraryRegistry.Entry(scope, scope + " (fixed version) — timing",
                        spec, one.toString(), sources, dir.resolve("out").toString(), null);
                row(scope, t.getName(), t.getCalls().size(), run, e);
            }
        } else if (args[0].equals("lib")) {
            LibraryRegistry.Entry base = LibraryRegistry.byKey(args[1]);
            Path scratch = Paths.get(args[2]);
            String run = args[3];
            List<TestStringAst> all = TestStringParser.parseAll(read(base.testsPath), base.testsPath);
            if (all.size() != 1) throw new IllegalStateException(base.testsPath + " has " + all.size() + " strings, expected 1");
            TestStringAst t = all.get(0);
            for (int i = 4; i < args.length; i++) {
                // a prefix longer than the string (ticketservice has 23 calls) is the whole string, run once
                int n = Math.min(Integer.parseInt(args[i]), t.getCalls().size());
                if (i > 4 && n == Math.min(Integer.parseInt(args[i - 1]), t.getCalls().size())) continue;
                Path dir = scratch.resolve(base.key).resolve("run" + run).resolve("p" + n);
                Path one = dir.resolve("prefix.tests");
                Files.createDirectories(dir);
                Files.write(one, testText(t.getName(), t.getCalls().subList(0, n)).getBytes(StandardCharsets.UTF_8));
                LibraryRegistry.Entry e = LibraryRegistry.variantOf(base, base.librarySourcePaths, one.toString(),
                        dir.resolve("out").toString());
                row(base.key, t.getName() + "@" + n, n, run, e);
            }
        } else {
            throw new IllegalArgumentException("mode d4j or lib");
        }
        System.exit(0);
    }

    private static void row(String scope, String name, int calls, String run, LibraryRegistry.Entry e) throws Exception {
        long w0 = System.currentTimeMillis(), t0 = System.nanoTime();
        GenatcMain.LibraryOutcome o = GenatcMain.generate(e, new File(e.outputDir), TestPlan.JUNIT_CLASS, false);
        long total = (System.nanoTime() - t0) / 1_000_000L, wall = System.currentTimeMillis() - w0;
        GenatcMain.SequenceOutcome s = o.sequences.isEmpty() ? null : o.sequences.get(0);
        System.out.println("ROW\t" + scope + "\t" + name + "\t" + calls + "\t" + run + "\t"
                + (s == null ? o.status : s.status) + "\t" + (s == null ? 0 : s.rounds) + "\t" + (s == null ? 0 : s.spfRuns)
                + "\t" + (s == null ? 0 : s.spfMillis) + "\t" + total + "\t" + wall + "\t"
                + (s == null || s.detail == null ? "" : s.detail.replace('\t', ' ').replace('\n', ' ')));
    }

    private static String testText(String name, List<String> calls) {
        return "test " + name + " {\n    sequence: " + String.join(" -> ", calls) + ";\n}\n";
    }

    private static String read(String p) throws Exception {
        return new String(Files.readAllBytes(Paths.get(p)), StandardCharsets.UTF_8);
    }

    private FairTiming() {}
}
