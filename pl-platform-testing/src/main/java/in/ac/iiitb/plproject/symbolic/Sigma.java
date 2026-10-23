package in.ac.iiitb.plproject.symbolic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * sigma: the declared type of every variable of the test case, and what
 * symbolic execution currently knows about its value — nothing yet, a
 * symbolic value, or a concrete one. A concrete value produced by a real call
 * lives inside SPF; here it is only known to BE concrete ({@code literal}
 * stays null).
 */
public final class Sigma {

    public enum State { UNDEFINED, SYMBOLIC, CONCRETE }

    public static final class Value {
        public final State state;
        /** The literal, when it is known outside SPF (an assigned input). */
        public final String literal;

        private Value(State state, String literal) {
            this.state = state;
            this.literal = literal;
        }

        public static final Value UNDEFINED = new Value(State.UNDEFINED, null);
        public static final Value SYMBOLIC = new Value(State.SYMBOLIC, null);
        public static Value concrete(String literal) { return new Value(State.CONCRETE, literal); }

        public boolean isSymbolic() { return state == State.SYMBOLIC; }
        @Override public String toString() {
            return state == State.CONCRETE ? (literal != null ? literal : "<concrete>") : state.name().toLowerCase();
        }
    }

    private final Map<String, String> types = new LinkedHashMap<String, String>();
    private final Map<String, Value> values = new LinkedHashMap<String, Value>();

    public void declare(String var, String type, Value value) {
        types.put(var, type);
        values.put(var, value);
    }

    public String typeOf(String var) { return types.get(var); }

    public Value get(String var) {
        Value v = values.get(var);
        return v != null ? v : Value.UNDEFINED;
    }

    public void set(String var, Value value) { values.put(var, value); }

    public Map<String, String> types() { return types; }

    public Sigma copy() {
        Sigma s = new Sigma();
        s.types.putAll(types);
        s.values.putAll(values);
        return s;
    }
}
