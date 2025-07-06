package Cse_Machine;

import Abstract_Syntax_Tree.ASTNode;
import Abstract_Syntax_Tree.ASTNodeType;

import java.util.Stack;

/**
 * Handles conditional expressions ('cond -> then | else') in RPAL by ensuring the condition is evaluated first
 * and only the relevant branch ('then' or 'else') is executed, preventing infinite recursion.
 */

public class Beta_Condition_Eval extends ASTNode {
    private Stack<ASTNode> thenBody;
    private Stack<ASTNode> elseBody;

    public Beta_Condition_Eval() {
        setTypeOfASTNode(ASTNodeType.BETA);
        thenBody = new Stack<ASTNode>();
        elseBody = new Stack<ASTNode>();
    }

    public Beta_Condition_Eval acceptASTNode(Node_Duplication nodeCopier) {
        return nodeCopier.takecopy(this);
    }

    public Stack<ASTNode> getThenBody() {
        return thenBody;
    }

    public void setThenBody(Stack<ASTNode> thenBody) {
        this.thenBody = thenBody;
    }

    public Stack<ASTNode> getElseBody() {
        return elseBody;
    }

    public void setElseBody(Stack<ASTNode> elseBody) {
        this.elseBody = elseBody;
    }

}
