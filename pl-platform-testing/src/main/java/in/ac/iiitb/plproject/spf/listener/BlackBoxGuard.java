package in.ac.iiitb.plproject.spf.listener;

import gov.nasa.jpf.Config;
import gov.nasa.jpf.JPF;
import gov.nasa.jpf.ListenerAdapter;
import gov.nasa.jpf.vm.ClassInfo;
import gov.nasa.jpf.vm.ElementInfo;
import gov.nasa.jpf.vm.FieldInfo;
import gov.nasa.jpf.vm.Instruction;
import gov.nasa.jpf.vm.MJIEnv;
import gov.nasa.jpf.vm.MethodInfo;
import gov.nasa.jpf.vm.StackFrame;
import gov.nasa.jpf.vm.ThreadInfo;
import gov.nasa.jpf.vm.VM;
import gov.nasa.jpf.vm.bytecode.FieldInstruction;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * The black-box guard: no symbolic value may ever reach the library. On entry
 * to every method of a library class it checks each argument, and each field
 * of an object passed as an argument; on every field read executed inside a
 * library class it checks the value read. Anything carrying a jpf-symbc
 * symbolic attribute is a violation: it is written to
 * {@code genatc.blackbox_output} and the search is stopped, so the run fails.
 *
 * <p>The library classes are named by {@code genatc.library_classes}
 * (comma-separated, fully qualified).
 */
public class BlackBoxGuard extends ListenerAdapter {

    private final Set<String> libraryClasses;
    private final String outputPath;
    private boolean violated;

    public BlackBoxGuard(Config conf, JPF jpf) {
        this(conf);
    }

    public BlackBoxGuard(Config conf) {
        String[] classes = conf.getStringArray("genatc.library_classes");
        this.libraryClasses = new HashSet<String>(classes == null ? java.util.Collections.<String>emptyList()
                                                                  : Arrays.asList(classes));
        this.outputPath = conf.getString("genatc.blackbox_output", "genatc-blackbox.txt");
    }

    private boolean isLibrary(ClassInfo ci) {
        return ci != null && libraryClasses.contains(ci.getName());
    }

    @Override
    public void methodEntered(VM vm, ThreadInfo ti, MethodInfo mi) {
        if (violated || !isLibrary(mi.getClassInfo())) return;
        StackFrame frame = ti.getTopFrame();
        if (frame == null || frame.getMethodInfo() != mi) return;
        for (int slot = 0; slot < mi.getArgumentsSize(); slot++) {
            if (isSymbolic(frame.getLocalAttr(slot))) {
                violation(vm, "argument slot " + slot + " of " + mi.getFullName() + " is symbolic: "
                        + frame.getLocalAttr(slot));
                return;
            }
            if (frame.isLocalVariableRef(slot)) {
                String where = checkObject(vm, frame.getLocalVariable(slot));
                if (where != null) {
                    violation(vm, "argument slot " + slot + " of " + mi.getFullName() + ": " + where);
                    return;
                }
            }
        }
    }

    @Override
    public void instructionExecuted(VM vm, ThreadInfo ti, Instruction next, Instruction executed) {
        if (violated || !(executed instanceof FieldInstruction)) return;
        FieldInstruction field = (FieldInstruction) executed;
        if (!field.isRead() || !isLibrary(executed.getMethodInfo().getClassInfo())) return;
        StackFrame frame = ti.getTopFrame();
        if (frame == null || frame.getMethodInfo() != executed.getMethodInfo()) return; // it threw
        FieldInfo fi = field.getFieldInfo();
        Object attr = fi != null && !fi.is1SlotField() ? frame.getLongOperandAttr() : frame.getOperandAttr();
        if (isSymbolic(attr)) {
            violation(vm, "field read of " + (fi != null ? fi.getFullName() : "?") + " in "
                    + executed.getMethodInfo().getFullName() + " is symbolic: " + attr);
        } else if (fi != null && fi.isReference()) {
            String where = checkObject(vm, frame.peek());
            if (where != null) violation(vm, "field read of " + fi.getFullName() + ": " + where);
        }
    }

    /** Where a symbolic attribute sits on the object {@code ref} (its own or a field's), or null. */
    private static String checkObject(VM vm, int ref) {
        if (ref == MJIEnv.NULL) return null;
        ElementInfo ei = vm.getHeap().get(ref);
        if (ei == null) return null;
        if (isSymbolic(ei.getObjectAttr())) return "object " + ei + " is symbolic: " + ei.getObjectAttr();
        if (ei.isArray()) {
            for (int i = 0; i < ei.arrayLength(); i++) {
                if (isSymbolic(ei.getElementAttr(i))) return "element " + i + " of " + ei + " is symbolic";
            }
            return null;
        }
        ClassInfo ci = ei.getClassInfo();
        for (int i = 0; i < ci.getNumberOfInstanceFields(); i++) {
            FieldInfo fi = ci.getInstanceField(i);
            if (isSymbolic(ei.getFieldAttr(fi))) {
                return "field " + fi.getName() + " of " + ei + " is symbolic: " + ei.getFieldAttr(fi);
            }
        }
        return null;
    }

    /**
     * jpf-symbc marks a symbolic value with an attribute from its own packages;
     * its {@code *Constant} expressions are concrete values and do not count.
     */
    static boolean isSymbolic(Object attr) {
        return attr != null && attr.getClass().getName().startsWith("gov.nasa.jpf.symbc.")
                && !attr.getClass().getSimpleName().endsWith("Constant");
    }

    private void violation(VM vm, String what) {
        violated = true;
        try (PrintWriter out = new PrintWriter(new FileWriter(outputPath, true))) {
            out.println("BLACK-BOX VIOLATION: " + what);
        } catch (IOException e) {
            throw new RuntimeException("BlackBoxGuard: cannot write " + outputPath, e);
        }
        vm.getSearch().terminate();
    }
}
