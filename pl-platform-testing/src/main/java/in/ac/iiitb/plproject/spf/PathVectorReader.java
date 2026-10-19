package in.ac.iiitb.plproject.spf;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reads {@link in.ac.iiitb.plproject.spf.listener.PathVectorListener}'s
 * output: one small, fixed-schema JSON object per line, written by this
 * project's own code. A hand-rolled parser is appropriate here specifically
 * BECAUSE the schema is fixed and owned end to end — this is the opposite of
 * scraping SPF's own free-form console report (rule 7), which is exactly what
 * writing this file in the first place avoids.
 */
public final class PathVectorReader {

    private PathVectorReader() {}

    public static List<PathVector> read(Path jsonl) throws IOException {
        List<PathVector> out = new ArrayList<PathVector>();
        if (!Files.exists(jsonl)) return out;
        for (String line : Files.readAllLines(jsonl, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (trimmed.isEmpty()) continue;
            out.add(parseLine(trimmed));
        }
        return out;
    }

    private static PathVector parseLine(String line) {
        Map<String, Object> obj = parseObject(line, new int[]{0});
        String event = (String) obj.get("event");
        if ("path".equals(event)) {
            int pathId = ((Number) obj.get("path")).intValue();
            boolean sat = Boolean.TRUE.equals(obj.get("sat"));
            @SuppressWarnings("unchecked")
            Map<String, Object> rawVars = (Map<String, Object>) obj.get("vars");
            Map<String, String> vars = new LinkedHashMap<String, String>();
            if (rawVars != null) {
                for (Map.Entry<String, Object> e : rawVars.entrySet()) {
                    vars.put(e.getKey(), String.valueOf(e.getValue()));
                }
            }
            return PathVector.path(pathId, sat, vars);
        } else if ("searchConstraintHit".equals(event)) {
            return PathVector.constraintHit((String) obj.get("detail"));
        } else if ("error".equals(event)) {
            return PathVector.error((String) obj.get("detail"));
        } else if ("searchFinished".equals(event)) {
            return PathVector.finished(((Number) obj.get("paths")).intValue());
        }
        throw new IllegalArgumentException("unknown event in: " + line);
    }

    // ── A tiny recursive-descent JSON reader, flat objects/strings/bools/numbers only ──

    private static Map<String, Object> parseObject(String s, int[] pos) {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        expect(s, pos, '{');
        skipWs(s, pos);
        if (peek(s, pos) == '}') { pos[0]++; return result; }
        while (true) {
            skipWs(s, pos);
            String key = parseString(s, pos);
            skipWs(s, pos);
            expect(s, pos, ':');
            skipWs(s, pos);
            Object value = parseValue(s, pos);
            result.put(key, value);
            skipWs(s, pos);
            char c = peek(s, pos);
            if (c == ',') { pos[0]++; continue; }
            if (c == '}') { pos[0]++; break; }
            throw new IllegalArgumentException("malformed JSON at " + pos[0] + " in: " + s);
        }
        return result;
    }

    private static Object parseValue(String s, int[] pos) {
        char c = peek(s, pos);
        if (c == '"') return parseString(s, pos);
        if (c == '{') return parseObject(s, pos);
        if (c == 't') { pos[0] += 4; return Boolean.TRUE; }
        if (c == 'f') { pos[0] += 5; return Boolean.FALSE; }
        if (c == 'n') { pos[0] += 4; return null; }
        // number
        int start = pos[0];
        while (pos[0] < s.length() && "-+.0123456789eE".indexOf(s.charAt(pos[0])) >= 0) pos[0]++;
        return Double.parseDouble(s.substring(start, pos[0]));
    }

    private static String parseString(String s, int[] pos) {
        expect(s, pos, '"');
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = s.charAt(pos[0]++);
            if (c == '"') break;
            if (c == '\\') {
                char n = s.charAt(pos[0]++);
                if (n == 'n') sb.append('\n');
                else sb.append(n);
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    private static void skipWs(String s, int[] pos) {
        while (pos[0] < s.length() && Character.isWhitespace(s.charAt(pos[0]))) pos[0]++;
    }

    private static char peek(String s, int[] pos) {
        return s.charAt(pos[0]);
    }

    private static void expect(String s, int[] pos, char c) {
        if (s.charAt(pos[0]) != c) {
            throw new IllegalArgumentException("expected '" + c + "' at " + pos[0] + " in: " + s);
        }
        pos[0]++;
    }
}
