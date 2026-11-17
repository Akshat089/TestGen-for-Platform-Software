package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.mutation.TestCache;
import in.ac.iiitb.plproject.mutation.TestStringGenerator;
import in.ac.iiitb.plproject.parser.SpecToAstConverter;
import in.ac.iiitb.plproject.parser.TestStringParser;
import in.ac.iiitb.plproject.parser.ast.JmlSpecAst;
import in.ac.iiitb.plproject.parser.ast.TestStringAst;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Step 7 of the mutation plan: a generated FAMILY of TestGen test strings for one
 * Defects4J class, as phase 1 of {@code GeneratedSuiteRunner} does for the 14 own
 * libraries.
 *
 * <p>{@link TestStringGenerator} draws the family from the spec, seeded (for its
 * call order only) with the class's LONG test string, never the bug-focused short
 * ones, so the family is blind. Each string's test is generated ONCE by
 * {@link TestCache} on the fixed version and run there; only the passing ones are
 * kept, and their sources are copied into one folder that mutation.sh runs as the
 * suite {@code testgen-family} against every mutant.
 *
 * <pre>
 *   D4jFamily &lt;key&gt; &lt;spec&gt; &lt;long.tests&gt; &lt;sources.txt&gt; &lt;workDir&gt; &lt;suiteDir&gt; &lt;report.tsv&gt; [count=260]
 * </pre>
 * It lives in TestGen's {@code atc} package only to build a {@link LibraryRegistry.Entry}
 * for a class TestGen does not list, like {@code D4jMain}.
 */
public final class D4jFamily {

    public static void main(String[] args) throws Exception {
        if (args.length < 7) {
            System.err.println("usage: D4jFamily <key> <spec> <long.tests> <sources.txt> <workDir> <suiteDir> <report.tsv> [count]");
            System.exit(64);
        }
        String key = args[0];
        String spec = args[1];
        String longTests = args[2];
        List<String> sources = new ArrayList<String>();
        for (String l : Files.readAllLines(Paths.get(args[3]), StandardCharsets.UTF_8)) {
            if (!l.trim().isEmpty()) sources.add(l.trim());
        }
        Path work = Paths.get(args[4]).toAbsolutePath();
        Path suiteDir = Paths.get(args[5]).toAbsolutePath();
        Path report = Paths.get(args[6]);
        int count = args.length > 7 ? Integer.parseInt(args[7]) : 260;

        JmlSpecAst specAst = SpecToAstConverter.convertSpecToAst(read(spec));
        TestStringAst seed = TestStringParser.parse(read(longTests));
        // seeded with the key, as GeneratedSuiteRunner does: the same family on every machine
        List<String> texts = new TestStringGenerator(specAst, seed, key.hashCode())
                .generate(Character.toUpperCase(key.charAt(0)) + key.substring(1) + "Generated", count);

        LibraryRegistry.Entry entry = new LibraryRegistry.Entry(key, key + " (Defects4J, family)", spec,
                longTests, sources, work.resolve("base").toString(), null);
        TestCache cache = new TestCache(entry, work.resolve("cache"));
        Path stringsDir = work.resolve("strings");
        Files.createDirectories(stringsDir);

        System.out.println(key + ": " + texts.size() + " test strings");
        long t0 = System.currentTimeMillis();
        List<String> names = new ArrayList<String>();
        List<String> sequences = new ArrayList<String>();
        List<TestCache.CachedTest> tests = new ArrayList<TestCache.CachedTest>();
        for (int i = 0; i < texts.size(); i++) {
            String name = String.format("%s-%03d", key, i + 1);
            Path tp = stringsDir.resolve(name + ".tests");
            Files.write(tp, texts.get(i).getBytes(StandardCharsets.UTF_8));
            TestCache.CachedTest t;
            try {
                t = cache.generate(name, tp.toString());
            } catch (Exception e) {
                t = null;
            }
            names.add(name);
            sequences.add(texts.get(i).replaceAll("(?s).*sequence:\\s*", "").replaceAll("(?s);.*", "").trim());
            tests.add(t);
            if ((i + 1) % 20 == 0) {
                System.out.println(key + ": generated " + (i + 1) + "/" + texts.size() + " ("
                        + (System.currentTimeMillis() - t0) / 1000 + " s)");
            }
        }

        // run every generated test on the fixed version, 50 per JVM
        List<String> classes = new ArrayList<String>();
        for (TestCache.CachedTest t : tests) if (t != null && t.hasTest()) classes.add(t.className);
        Map<String, TestCache.TestRun> runs = new LinkedHashMap<String, TestCache.TestRun>();
        for (int from = 0; from < classes.size(); from += 50) {
            runs.putAll(cache.runOnCorrectLibrary(classes.subList(from, Math.min(classes.size(), from + 50))));
        }

        deleteRecursively(suiteDir);
        Files.createDirectories(suiteDir);
        StringBuilder tsv = new StringBuilder("name\tstatus\tsequence\tdetail\n");
        Map<String, Integer> counts = new LinkedHashMap<String, Integer>();
        for (int i = 0; i < names.size(); i++) {
            TestCache.CachedTest t = tests.get(i);
            String status, detail = "";
            if (t == null) {
                status = "INVALID"; detail = "the generator threw";
            } else if (!t.hasTest()) {
                status = t.status; detail = t.detail == null ? "" : t.detail;
            } else {
                TestCache.TestRun r = runs.get(t.className);
                if (r == null) { status = "INVALID"; detail = "the test did not run"; }
                else if (r.passed) {
                    status = "PASSED";
                    String simple = t.className.substring(t.className.lastIndexOf('.') + 1);
                    File src = new File(GenatcMain.generatedDir(work.resolve("cache").resolve("gen")
                            .resolve(names.get(i)).toFile()), simple + ".java");
                    Files.copy(src.toPath(), suiteDir.resolve(simple + ".java"), StandardCopyOption.REPLACE_EXISTING);
                } else { status = "FAILED"; detail = r.kind + ": " + r.message; }
            }
            counts.merge(status, 1, Integer::sum);
            tsv.append(names.get(i)).append('\t').append(status).append('\t').append(sequences.get(i)).append('\t')
               .append(firstLine(detail).replace('\t', ' ')).append('\n');
        }
        Files.write(report, tsv.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println(key + ": family " + counts + " (" + (System.currentTimeMillis() - t0) / 1000 + " s)");
    }

    private static String read(String p) throws java.io.IOException {
        return new String(Files.readAllBytes(Paths.get(p)), StandardCharsets.UTF_8);
    }

    private static String firstLine(String s) {
        int i = s.indexOf('\n');
        return i < 0 ? s : s.substring(0, i);
    }

    private static void deleteRecursively(Path f) throws java.io.IOException {
        if (!Files.exists(f)) return;
        try (java.util.stream.Stream<Path> w = Files.walk(f)) {
            for (Path x : (Iterable<Path>) w.sorted(java.util.Collections.reverseOrder())::iterator) Files.delete(x);
        }
    }

    private D4jFamily() {}
}
