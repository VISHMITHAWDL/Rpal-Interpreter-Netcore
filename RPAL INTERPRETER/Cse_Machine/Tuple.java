package Cse_Machine;

import Abstract_Syntax_Tree.ASTNode;
import Abstract_Syntax_Tree.ASTNodeType;

/**
 * Represents a linked sequence of AST nodes as a Tuple.
 * Renders itself in Lisp-style notation: (elem1, elem2, …).
 */
public class Tuple extends ASTNode {

    /**
     * Initialize this node’s type to TUPLE.
     */
    public Tuple() {
        setTypeOfASTNode(ASTNodeType.TUPLE);
    }

    /**
     * Builds a parenthesized, comma-separated string of values
     * from this tuple’s child and its siblings.
     *
     * @return "nil" if empty, otherwise "(v1, v2, ..., vN)"
     */
    @Override
    public String getValueOfASTNode() {
        ASTNode current = getChildOfASTNode();
        if (current == null) {
            return "nil";
        }

        StringBuilder builder = new StringBuilder("(");
        // Traverse through each sibling except the last
        while (current.getSiblingOfASTNode() != null) {
            builder.append(current.getValueOfASTNode())
                    .append(", ");
            current = current.getSiblingOfASTNode();
        }
        // Append the final element without a trailing comma
        builder.append(current.getValueOfASTNode())
                .append(")");
        return builder.toString();
    }

    /**
     * Delegate duplication to the provided Node_Duplication instance.
     */
    @Override
    public Tuple acceptASTNode(Node_Duplication copier) {
        return copier.takecopy(this);
    }
}

