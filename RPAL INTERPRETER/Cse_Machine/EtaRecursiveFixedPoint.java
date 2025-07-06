package Cse_Machine;

import Abstract_Syntax_Tree.ASTNode;
import Abstract_Syntax_Tree.ASTNodeType;

/**
 * Handles the application of the fixed-point operator (Y) to a function AST.
 * <p>
 * This node wraps a DeltaControlStructure, representing the “eta-expanded”
 * form of a recursive function. It does not itself compute the fixed point;
 * rather, it relies on the evaluation engine to select the non-recursive
 * branch at runtime to avoid infinite loops.
 * </p>
 * <p>
 * While this mechanism aids in managing recursive calls safely, it cannot
 * guard against deliberately infinite recursion in user code.
 * </p>
 */
public class EtaRecursiveFixedPoint extends ASTNode {
    private DeltaControlStructure delta;

    /**
     * Tag this node as an ETA (fixed-point) AST node.
     */
    public EtaRecursiveFixedPoint() {
        setTypeOfASTNode(ASTNodeType.ETA);
    }

    /**
     * When partially applied, shows the first bound variable of the
     * underlying delta and its identifier.
     */
    @Override
    public String getValueOfASTNode() {
        String varName = delta.getBoundVars().get(0);
        int id       = delta.getIndex();
        return "[eta closure: " + varName + ": " + id + "]";
    }

    /**
     * Delegate copying logic to the supplied Node_Duplication helper.
     */
    @Override
    public EtaRecursiveFixedPoint acceptASTNode(Node_Duplication copier) {
        return copier.takecopy(this);
    }

    /**
     * Retrieve the associated DeltaControlStructure (the “body” of this fixed-point).
     */
    public DeltaControlStructure getDelta() {
        return delta;
    }

    /**
     * Attach a DeltaControlStructure that this fixed-point node will wrap.
     */
    public void setDelta(DeltaControlStructure delta) {
        this.delta = delta;
    }
}
