package in.ac.iiitb.plproject.parser.ast;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JmlSpecAst {
    private List<JmlFunctionSpec> specs;

    /**
     * Global library state declared by the spec file's optional
     * {@code state { ... }} block, as name -> declared Java type.
     *
     * Example (Stack): {@code {S=List<String>, size=int, top=String}}.
     *
     * The library dry-run generator uses this for two things:
     * a name found here is qualified as {@code Helper.<name>} in the emitted
     * pre/postconditions, and its declared type types the {@code \old(...)}
     * snapshot local.  A spec file without the block behaves exactly as before.
     */
    private Map<String, String> stateVars = new LinkedHashMap<String, String>();

    public JmlSpecAst(List<JmlFunctionSpec> specs) {
        this.specs = specs;
    }

    /**
     * Starting values from the state block ({@code int size = 0;}), name -&gt;
     * initializer text. A state variable absent here starts from Java's
     * default: 0, false, null, or an empty collection.
     */
    private Map<String, String> stateInitializers = new LinkedHashMap<String, String>();

    public JmlSpecAst(List<JmlFunctionSpec> specs, Map<String, String> stateVars) {
        this(specs, stateVars, null);
    }

    public JmlSpecAst(List<JmlFunctionSpec> specs, Map<String, String> stateVars,
                      Map<String, String> stateInitializers) {
        this.specs = specs;
        if (stateVars != null) this.stateVars = new LinkedHashMap<String, String>(stateVars);
        if (stateInitializers != null) {
            this.stateInitializers = new LinkedHashMap<String, String>(stateInitializers);
        }
    }

    /** Declared starting values, name -&gt; initializer text. Empty when none is written. */
    public Map<String, String> getStateInitializers() {
        return Collections.unmodifiableMap(stateInitializers);
    }

    /** Declared global state, name -> type. Empty when the spec declares none. */
    public Map<String, String> getStateVars() {
        return Collections.unmodifiableMap(stateVars);
    }

    /** Declared type of a state variable, or null when it is not library state. */
    public String getStateVarType(String name) {
        return stateVars.get(name);
    }

    public List<JmlFunctionSpec> getSpecs() {
        return specs;
    }

    public JmlFunctionSpec findSpecFor(String functionName) {
        // Placeholder implementation
        for (JmlFunctionSpec spec : specs) {
            if (spec.getName().equals(functionName)) {
                return spec;
            }
        }
        return null;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("JmlSpecAst[");
        if (specs != null) {
            for (int i = 0; i < specs.size(); i++) {
                if (i > 0) sb.append(", ");
                sb.append(specs.get(i));
            }
        }
        sb.append("]");
        if (!stateVars.isEmpty()) {
            sb.append(" state").append(stateVars);
        }
        return sb.toString();
    }
}
