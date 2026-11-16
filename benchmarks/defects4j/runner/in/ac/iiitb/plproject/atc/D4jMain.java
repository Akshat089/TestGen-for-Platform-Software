package in.ac.iiitb.plproject.atc;

import in.ac.iiitb.plproject.ir.TestPlan;
import in.ac.iiitb.plproject.verify.GeneratedSuiteHarness;
import in.ac.iiitb.plproject.verify.JunitJars;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Runs TestGen on one Defects4J bug, following Defects4J's protocol:
 * generate the tests on the FIXED version, drop every test that fails there
 * (a false alarm), then run the rest on the BUGGY version. The bug is detected
 * if at least one remaining test fails on the buggy version.
 *
 * <p>It lives in TestGen's {@code atc} package only so it can build a
 * {@link LibraryRegistry.Entry} for a library that is not one of the 14
 * registered ones; it changes nothing in TestGen. The library "sources" of
 * the entry are the adapter Helper.java plus the Commons Collections source
 * files it needs, taken from the Defects4J checkout (listed by run.sh).
 *
 * <pre>
 *   D4jMain &lt;bug-name&gt; &lt;spec&gt; &lt;tests&gt; &lt;outputDir&gt; &lt;fixed-sources.txt&gt; &lt;buggy-sources.txt&gt;
 * </pre>
 * Exit code: 0 detected, 1 not detected, 2 no usable test.
 */
public final class D4jMain {

    public static void main(String[] args) throws Exception {
        if (args.length != 6) {
            System.err.println("usage: D4jMain <bug-name> <spec> <tests> <outputDir> <fixed-sources.txt> <buggy-sources.txt>");
            System.exit(64);
        }
        String bug = args[0];
        String spec = args[1];
        String tests = args[2];
        File out = new File(args[3]);
        List<String> fixedSources = readLines(args[4]);
        List<String> buggySources = readLines(args[5]);

        // 1. Generate on the fixed version and run the tests there (GenatcMain.runOne).
        LibraryRegistry.Entry fixed = new LibraryRegistry.Entry(bug, bug + " (fixed version) — Defects4J",
                spec, tests, fixedSources, new File(out, "fixed").getPath(), null);
        GenatcMain.LibraryOutcome gen = GenatcMain.runOne(fixed);

        StringBuilder report = new StringBuilder();
        report.append("bug: ").append(bug).append('\n');
        for (GenatcMain.SequenceOutcome s : gen.sequences) {
            report.append(String.format("  sequence %-24s %s%s%n", s.name, s.status,
                    s.detail == null || s.detail.isEmpty() ? "" : "  (" + firstLine(s.detail) + ")"));
        }
        if (gen.junitSource == null || !gen.junitCompiled) {
            report.append("result: NO_TEST (").append(gen.status).append(")\n");
            finish(out, report, 2);
            return;
        }

        Set<String> falseAlarms = new HashSet<String>();
        for (String f : gen.junitFailures) falseAlarms.add(header(f));
        int kept = gen.junitRunCount - falseAlarms.size();
        report.append("tests generated: ").append(gen.junitRunCount)
              .append(", false alarms on fixed version (dropped): ").append(falseAlarms.size()).append('\n');
        for (String f : gen.junitFailures) report.append("  dropped  ").append(firstLine(f)).append('\n');

        // 2. Run the same JUnit class, unchanged, on the buggy version.
        File genDir = GenatcMain.generatedDir(new File(out, "fixed"));
        File buggyDir = GenatcMain.generatedDir(new File(out, "buggy"));
        deleteRecursively(new File(out, "buggy"));
        Files.createDirectories(buggyDir.toPath());
        String junitFile = gen.junitClassName + ".java";
        Files.copy(new File(genDir, junitFile).toPath(), new File(buggyDir, junitFile).toPath());
        for (String s : buggySources) {
            Path src = Paths.get(s);
            Files.copy(src, new File(buggyDir, src.getFileName().toString()).toPath(),
                    StandardCopyOption.REPLACE_EXISTING);
        }
        GeneratedSuiteHarness.VerifyResult onBuggy = new GeneratedSuiteHarness().compileAndRun(
                buggyDir, JunitJars.resolve(), TestPlan.GENERATED_PACKAGE + "." + gen.junitClassName);
        if (!onBuggy.compiled) {
            report.append("result: ERROR (tests do not compile against the buggy version)\n")
                  .append(onBuggy.compileError).append('\n');
            finish(out, report, 2);
            return;
        }

        int detecting = 0;
        for (String f : onBuggy.failures) {
            boolean dropped = falseAlarms.contains(header(f));
            if (!dropped) detecting++;
            report.append(dropped ? "  ignored  " : "  FAILS    ").append(firstLine(f)).append('\n');
        }
        report.append("tests kept: ").append(kept).append(", failing on buggy version: ").append(detecting).append('\n');
        report.append("result: ").append(detecting > 0 ? "DETECTED" : "NOT DETECTED").append('\n');
        finish(out, report, detecting > 0 ? 0 : 1);
    }

    /** "methodName(Class): message" -> "methodName(Class)". */
    private static String header(String failure) {
        int i = failure.indexOf("): ");
        return i < 0 ? failure : failure.substring(0, i + 1);
    }

    private static String firstLine(String s) {
        int i = s.indexOf('\n');
        return i < 0 ? s : s.substring(0, i);
    }

    private static void finish(File out, StringBuilder report, int code) throws IOException {
        Files.createDirectories(out.toPath());
        Files.write(new File(out, "result.txt").toPath(), report.toString().getBytes(StandardCharsets.UTF_8));
        System.out.println("\n---------------- Defects4J result ----------------");
        System.out.print(report);
        System.exit(code);
    }

    private static List<String> readLines(String path) throws IOException {
        List<String> out = new ArrayList<String>();
        for (String line : Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8)) {
            if (!line.trim().isEmpty()) out.add(line.trim());
        }
        return out;
    }

    private static void deleteRecursively(File f) throws IOException {
        if (!f.exists()) return;
        File[] kids = f.listFiles();
        if (kids != null) for (File k : kids) deleteRecursively(k);
        Files.delete(f.toPath());
    }

    private D4jMain() {}
}
