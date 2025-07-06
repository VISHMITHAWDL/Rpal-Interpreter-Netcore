package Cse_Machine;

import Abstract_Syntax_Tree.ASTNode;
import Abstract_Syntax_Tree.ASTNodeType;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Models a lambda‐style closure, bundling:
 *  - its bound variable names,
 *  - the environment where it was created,
 *  - the AST nodes forming its body, and
 *  - a numeric identifier.
 */
public class DeltaControlStructure extends ASTNode {
    private List<String> boundVars;
    private Environment linkedEnv;       // Environment captured at creation
    private Stack<ASTNode> body;         // AST nodes making up the closure’s body
    private int index;                   // Closure identifier

    /**
     * Create a new DELTA closure node and initialize its variable list.
     */
    public DeltaControlStructure() {
        setTypeOfASTNode(ASTNodeType.DELTA);
        this.boundVars = new ArrayList<>();
    }

    /**
     * Duplicate this node via the provided copier.
     */
    public DeltaControlStructure acceptASTNode(Node_Duplication copier) {
        return copier.takecopy(this);
    }

    /**
     * When partially applied, shows the first bound variable and this closure’s index.
     */
    @Override
    public String getValueOfASTNode() {
        return "[lambda closure: " + boundVars.get(0) + ": " + index + "]";
    }

    //––– boundVars accessors –––
    public List<String> getBoundVars() {
        return boundVars;
    }

    public void setBoundVars(List<String> vars) {
        this.boundVars = vars;
    }

    public void addBoundVars(String var) {
        boundVars.add(var);
    }

    //––– body accessors –––
    public Stack<ASTNode> getBody() {
        return body;
    }

    public void setBody(Stack<ASTNode> codeBody) {
        this.body = codeBody;
    }

    //––– index accessors –––
    public int getIndex() {
        return index;
    }

    public void setIndex(int idx) {
        this.index = idx;
    }

    //––– linkedEnv accessors –––
    public Environment getLinkedEnv() {
        return linkedEnv;
    }

    public void setLinkedEnv(Environment env) {
        this.linkedEnv = env;
    }
}

