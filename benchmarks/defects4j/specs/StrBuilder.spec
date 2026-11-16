/*
 * DEFECTS4J — StrBuilder (Apache Commons Lang 2.x)
 * Real bugs: Lang-59, Lang-61.  Adapter: libraries/strbuilder/Helper.java
 *
 * The builder's content is copied into the String `content` after every
 * change, so a postcondition can compare the builder with java.lang.String,
 * which is the reference: sb.indexOf(s) must agree with content.indexOf(s).
 *
 *   Lang-61  indexOf/deleteAll search the whole buffer, not just the content,
 *            so after clear/setLength/delete they find text that is gone
 *            -> pos == content.indexOf(s) and pos < length fail after indexOf
 *   Lang-59  appendFixedWidthPadRight copies the whole string even when it is
 *            longer than width; with a small buffer that writes past its end
 *            -> the call throws ArrayIndexOutOfBoundsException
 *
 * Search and delete take a string that was appended earlier (Appended), the
 * only way to make them find something: a precondition cannot ask for a
 * substring of content. create(capacity) lets TestGen choose the initial
 * buffer size instead of the adapter fixing one.
 */

state {
    String content;
    int length;
    List<String> Appended;
}

spec create {
    signature: void create(int capacity);
    requires:  capacity >= 1;
    ensures:   length == 0;
}

spec append {
    signature: void append(String s);
    requires:  s != null;
    ensures:   content.endsWith(s) && length == \old(length) + s.length();
}

spec appendPadRight {
    signature: void appendPadRight(String s, int width);
    requires:  s != null && width >= 0;
    ensures:   length == \old(length) + width;
}

spec deleteFirst {
    signature: void deleteFirst(String s);
    requires:  Appended.contains(s);
    ensures:   length <= \old(length);
}

spec deleteAll {
    signature: void deleteAll(String s);
    requires:  Appended.contains(s);
    ensures:   content.indexOf(s) == -1 && length <= \old(length);
}

spec indexOf {
    signature: int indexOf(String s);
    requires:  Appended.contains(s);
    ensures:   \result == pos && pos == content.indexOf(s) && pos < length && length == \old(length);
}

spec contains {
    signature: boolean contains(String s);
    requires:  Appended.contains(s);
    ensures:   \result == present && present == content.contains(s) && length == \old(length);
}

spec setLength {
    signature: void setLength(int n);
    requires:  n >= 0 && n <= length;
    ensures:   length == n;
}

spec clear {
    signature: void clear();
    requires:  length >= 0;
    ensures:   length == 0;
}

spec count {
    signature: int count();
    requires:  length >= 0;
    ensures:   \result == n && n == length;
}
