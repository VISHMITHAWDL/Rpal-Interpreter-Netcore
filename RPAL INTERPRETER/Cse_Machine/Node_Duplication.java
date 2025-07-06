package Cse_Machine;

import Abstract_Syntax_Tree.ASTNode;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Provides deep-copy functionality for various AST node types.
 * Ensures that each clone has its own child/sibling chains
 * and, where applicable, duplicated bodies or bindings.
 */
public class Node_Duplication {

    /**
     * Clone a generic ASTNode, including its child and sibling subtrees.
     */
    public ASTNode takecopy(ASTNode original) {
        ASTNode cloned = new ASTNode();
        // duplicate the child subtree
        if (original.getChildOfASTNode() != null) {
            cloned.setChildOfASTNode(
                    original.getChildOfASTNode().acceptASTNode(this)
            );
        }
        // duplicate the sibling subtree
        if (original.getSiblingOfASTNode() != null) {
            cloned.setSiblingOfASTNode(
                    original.getSiblingOfASTNode().acceptASTNode(this)
            );
        }
        // copy over basic node fields
        cloned.setTypeOfASTNode(original.getTypeOfASTNode());
        cloned.setValueOfASTNode(original.getValueOfASTNode());
        cloned.setLineNumberOfSourceFile(
                original.getLineNumberOfSourceFile()
        );
        return cloned;
    }

    /**
     * Clone a Beta_Condition_Eval node, copying its branches' bodies.
     */
    public Beta_Condition_Eval takecopy(Beta_Condition_Eval source) {
        Beta_Condition_Eval cloned = new Beta_Condition_Eval();
        if (source.getChildOfASTNode() != null) {
            cloned.setChildOfASTNode(
                    source.getChildOfASTNode().acceptASTNode(this)
            );
        }
        if (source.getSiblingOfASTNode() != null) {
            cloned.setSiblingOfASTNode(
                    source.getSiblingOfASTNode().acceptASTNode(this)
            );
        }
        cloned.setTypeOfASTNode(source.getTypeOfASTNode());
        cloned.setValueOfASTNode(source.getValueOfASTNode());
        cloned.setLineNumberOfSourceFile(
                source.getLineNumberOfSourceFile()
        );

        // duplicate the 'then' body sequence
        Stack<ASTNode> thenCopy = new Stack<>();
        for (ASTNode step : source.getThenBody()) {
            thenCopy.add(step.acceptASTNode(this));
        }
        cloned.setThenBody(thenCopy);

        // duplicate the 'else' body sequence
        Stack<ASTNode> elseCopy = new Stack<>();
        for (ASTNode step : source.getElseBody()) {
            elseCopy.add(step.acceptASTNode(this));
        }
        cloned.setElseBody(elseCopy);

        return cloned;
    }

    /**
     * Clone an EtaRecursiveFixedPoint node, preserving its linked delta.
     */
    public EtaRecursiveFixedPoint takecopy(EtaRecursiveFixedPoint source) {
        EtaRecursiveFixedPoint cloned = new EtaRecursiveFixedPoint();
        if (source.getChildOfASTNode() != null) {
            cloned.setChildOfASTNode(
                    source.getChildOfASTNode().acceptASTNode(this)
            );
        }
        if (source.getSiblingOfASTNode() != null) {
            cloned.setSiblingOfASTNode(
                    source.getSiblingOfASTNode().acceptASTNode(this)
            );
        }
        cloned.setTypeOfASTNode(source.getTypeOfASTNode());
        cloned.setValueOfASTNode(source.getValueOfASTNode());
        cloned.setLineNumberOfSourceFile(
                source.getLineNumberOfSourceFile()
        );

        // link the duplicated delta structure
        cloned.setDelta(
                source.getDelta().acceptASTNode(this)
        );

        return cloned;
    }

    /**
     * Clone a DeltaControlStructure, including its body and bound variables.
     */
    public DeltaControlStructure takecopy(DeltaControlStructure source) {
        DeltaControlStructure cloned = new DeltaControlStructure();
        if (source.getChildOfASTNode() != null) {
            cloned.setChildOfASTNode(
                    source.getChildOfASTNode().acceptASTNode(this)
            );
        }
        if (source.getSiblingOfASTNode() != null) {
            cloned.setSiblingOfASTNode(
                    source.getSiblingOfASTNode().acceptASTNode(this)
            );
        }
        cloned.setTypeOfASTNode(source.getTypeOfASTNode());
        cloned.setValueOfASTNode(source.getValueOfASTNode());
        cloned.setLineNumberOfSourceFile(
                source.getLineNumberOfSourceFile()
        );
        cloned.setIndex(source.getIndex());

        // duplicate the closure's body sequence
        Stack<ASTNode> bodyClone = new Stack<>();
        for (ASTNode node : source.getBody()) {
            bodyClone.add(node.acceptASTNode(this));
        }
        cloned.setBody(bodyClone);

        // copy bound variable names
        List<String> varsClone = new ArrayList<>(source.getBoundVars());
        cloned.setBoundVars(varsClone);

        // reuse the same environment reference
        cloned.setLinkedEnv(source.getLinkedEnv());

        return cloned;
    }

    /**
     * Clone a Tuple node without additional nested structures.
     */
    public Tuple takecopy(Tuple source) {
        Tuple cloned = new Tuple();
        if (source.getChildOfASTNode() != null) {
            cloned.setChildOfASTNode(
                    source.getChildOfASTNode().acceptASTNode(this)
            );
        }
        if (source.getSiblingOfASTNode() != null) {
            cloned.setSiblingOfASTNode(
                    source.getSiblingOfASTNode().acceptASTNode(this)
            );
        }
        cloned.setTypeOfASTNode(source.getTypeOfASTNode());
        cloned.setValueOfASTNode(source.getValueOfASTNode());
        cloned.setLineNumberOfSourceFile(
                source.getLineNumberOfSourceFile()
        );
        return cloned;
    }
}

