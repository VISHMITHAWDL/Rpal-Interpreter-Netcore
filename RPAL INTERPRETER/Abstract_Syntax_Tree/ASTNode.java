package Abstract_Syntax_Tree;

import Cse_Machine.Node_Duplication;

public class ASTNode {
    private ASTNodeType typeOfASTNode;
    private String valueOfASTNode;
    private ASTNode childOfASTNode;
    private ASTNode siblingOfASTNode;
    private int lineNumberOfSourceFile;


    public ASTNode getChildOfASTNode() {
        return childOfASTNode;
    }

    public void setChildOfASTNode(ASTNode childOfASTNode) {
        this.childOfASTNode = childOfASTNode;
    }

    public ASTNode getSiblingOfASTNode() {
        return siblingOfASTNode;
    }

    public void setSiblingOfASTNode(ASTNode siblingOfASTNode) {
        this.siblingOfASTNode = siblingOfASTNode;
    }

    public ASTNodeType getTypeOfASTNode() {
        return typeOfASTNode;
    }

    public void setTypeOfASTNode(ASTNodeType typeOfASTNode) {
        this.typeOfASTNode = typeOfASTNode;
    }

    public String getValueOfASTNode() {
        return valueOfASTNode;
    }

    public void setValueOfASTNode(String valueOfASTNode) {
        this.valueOfASTNode = valueOfASTNode;
    }

    public ASTNode acceptASTNode(Node_Duplication nodeCopier) {
        return nodeCopier.takecopy(this);
    }

    public int getLineNumberOfSourceFile() {
        return lineNumberOfSourceFile;
    }

    public void setLineNumberOfSourceFile(int lineNumberOfSourceFile) {
        this.lineNumberOfSourceFile = lineNumberOfSourceFile;
    }
}
