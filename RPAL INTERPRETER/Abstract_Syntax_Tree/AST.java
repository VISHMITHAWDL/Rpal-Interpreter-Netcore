package Abstract_Syntax_Tree;

import Cse_Machine.Beta_Condition_Eval;
import Cse_Machine.DeltaControlStructure;
import java.util.ArrayDeque;
import java.util.Stack;

public class AST {
    private final ASTNode root;
    private ArrayDeque<PendingDeltaBody> pending_Delta_Body_Queue;
    private boolean Standardized;
    private DeltaControlStructure Cur_Delta;
    private DeltaControlStructure Root_Delta;
    private int Delta_Index;

    public AST(ASTNode node) {
        this.root = node;
    }

    /**
     * Print the entire AST to the console, one node per line,
     * with ‘.’ repeated for indentation.
     */
    public void printAST() {
        printNode(root, "");
    }

    // helper: preorder traversal, indent is a string of dots
    private void printNode(ASTNode node, String indent) {
        if (node == null) return;

        // print this node with the current indent
        PrintASTNodeDetails(node, indent);

        // recurse into children, each level adds one dot
        ASTNode child = node.getChildOfASTNode();
        while (child != null) {
            printNode(child, indent + ".");
            child = child.getSiblingOfASTNode();
        }
    }

    private void PrintASTNodeDetails(ASTNode node, String Print_Prefix) {
        if (node.getTypeOfASTNode() == ASTNodeType.IDENTIFIER ||
                node.getTypeOfASTNode() == ASTNodeType.INTEGER) {
            System.out.printf(Print_Prefix + node.getTypeOfASTNode().getPrintNameOfASTNode() + "\n", node.getValueOfASTNode());
        } else if (node.getTypeOfASTNode() == ASTNodeType.STRING)
            System.out.printf(Print_Prefix + node.getTypeOfASTNode().getPrintNameOfASTNode() + "\n", node.getValueOfASTNode());
        else {
            System.out.println(Print_Prefix + node.getTypeOfASTNode().getPrintNameOfASTNode());
        }
    }

    // standardizing the tree
    public void Standardize() {
        Standardize(root);
        Standardized = true;
    }

    private void Standardize(ASTNode node) {
        //standardizing the children first
        if (node.getChildOfASTNode() != null) {
            ASTNode Node_Child = node.getChildOfASTNode();
            while (Node_Child != null) {
                Standardize(Node_Child);
                Node_Child = Node_Child.getSiblingOfASTNode();
            }
        }

        //standardizing the node
        switch (node.getTypeOfASTNode()) {
            case LET:
                ASTNode equalNode = node.getChildOfASTNode();
                if (equalNode.getTypeOfASTNode() != ASTNodeType.EQUAL)
                    throw new RuntimeException("LET/WHERE: left child is not EQUAL"); //for safety
                ASTNode Node_1 = equalNode.getChildOfASTNode().getSiblingOfASTNode();
                equalNode.getChildOfASTNode().setSiblingOfASTNode(equalNode.getSiblingOfASTNode());
                equalNode.setSiblingOfASTNode(Node_1);
                equalNode.setTypeOfASTNode(ASTNodeType.LAMBDA);
                node.setTypeOfASTNode(ASTNodeType.GAMMA);
                break;

            case WHERE:
                equalNode = node.getChildOfASTNode().getSiblingOfASTNode();
                node.getChildOfASTNode().setSiblingOfASTNode(null);
                equalNode.setSiblingOfASTNode(node.getChildOfASTNode());
                node.setChildOfASTNode(equalNode);
                node.setTypeOfASTNode(ASTNodeType.LET);
                Standardize(node);
                break;

            case FCNFORM:
                ASTNode childSibling = node.getChildOfASTNode().getSiblingOfASTNode();
                node.getChildOfASTNode().setSiblingOfASTNode(constructLambdaChain(childSibling));
                node.setTypeOfASTNode(ASTNodeType.EQUAL);
                break;

            case AT:
                ASTNode Node1 = node.getChildOfASTNode();
                ASTNode Node_2 = Node1.getSiblingOfASTNode();
                ASTNode Node_3 = Node_2.getSiblingOfASTNode();
                ASTNode gammaNode = new ASTNode();
                gammaNode.setTypeOfASTNode(ASTNodeType.GAMMA);
                gammaNode.setChildOfASTNode(Node_2);
                Node_2.setSiblingOfASTNode(Node1);
                Node1.setSiblingOfASTNode(null);
                gammaNode.setSiblingOfASTNode(Node_3);
                node.setChildOfASTNode(gammaNode);
                node.setTypeOfASTNode(ASTNodeType.GAMMA);
                break;

            case WITHIN:
                if (node.getChildOfASTNode().getTypeOfASTNode() != ASTNodeType.EQUAL || node.getChildOfASTNode().getSiblingOfASTNode().getTypeOfASTNode() != ASTNodeType.EQUAL) {
                    throw new RuntimeException("WITHIN: one of the children is not EQUAL"); //for safety
                }
                ASTNode Node_4 = node.getChildOfASTNode().getChildOfASTNode();
                Node1 = Node_4.getSiblingOfASTNode();
                ASTNode Node_5 = node.getChildOfASTNode().getSiblingOfASTNode().getChildOfASTNode();
                Node_3 = Node_5.getSiblingOfASTNode();
                ASTNode lambdaNode = new ASTNode();
                lambdaNode.setTypeOfASTNode(ASTNodeType.LAMBDA);
                Node_4.setSiblingOfASTNode(Node_3);
                lambdaNode.setChildOfASTNode(Node_4);
                lambdaNode.setSiblingOfASTNode(Node1);
                gammaNode = new ASTNode();
                gammaNode.setTypeOfASTNode(ASTNodeType.GAMMA);
                gammaNode.setChildOfASTNode(lambdaNode);
                Node_5.setSiblingOfASTNode(gammaNode);
                node.setChildOfASTNode(Node_5);
                node.setTypeOfASTNode(ASTNodeType.EQUAL);
                break;
            case SIMULTDEF:

                ASTNode commaNode = new ASTNode();
                commaNode.setTypeOfASTNode(ASTNodeType.COMMA);
                ASTNode tauNode = new ASTNode();
                tauNode.setTypeOfASTNode(ASTNodeType.TAU);
                ASTNode childNode = node.getChildOfASTNode();
                while (childNode != null) {
                    populateCommaAndTauNode(childNode, commaNode, tauNode);
                    childNode = childNode.getSiblingOfASTNode();
                }
                commaNode.setSiblingOfASTNode(tauNode);
                node.setChildOfASTNode(commaNode);
                node.setTypeOfASTNode(ASTNodeType.EQUAL);
                break;
            case REC:

                childNode = node.getChildOfASTNode();
                if (childNode.getTypeOfASTNode() != ASTNodeType.EQUAL)
                    throw new RuntimeException("REC: child is not EQUAL"); //safety
                ASTNode x = childNode.getChildOfASTNode();
                lambdaNode = new ASTNode();
                lambdaNode.setTypeOfASTNode(ASTNodeType.LAMBDA);
                lambdaNode.setChildOfASTNode(x); //x is already attached to e
                ASTNode yStarNode = new ASTNode();
                yStarNode.setTypeOfASTNode(ASTNodeType.YSTAR);
                yStarNode.setSiblingOfASTNode(lambdaNode);
                gammaNode = new ASTNode();
                gammaNode.setTypeOfASTNode(ASTNodeType.GAMMA);
                gammaNode.setChildOfASTNode(yStarNode);
                ASTNode xWithSiblingGamma = new ASTNode();
                xWithSiblingGamma.setChildOfASTNode(x.getChildOfASTNode());
                xWithSiblingGamma.setSiblingOfASTNode(gammaNode);
                xWithSiblingGamma.setTypeOfASTNode(x.getTypeOfASTNode());
                xWithSiblingGamma.setValueOfASTNode(x.getValueOfASTNode());
                node.setChildOfASTNode(xWithSiblingGamma);
                node.setTypeOfASTNode(ASTNodeType.EQUAL);
                break;
            case LAMBDA:

                childSibling = node.getChildOfASTNode().getSiblingOfASTNode();
                node.getChildOfASTNode().setSiblingOfASTNode(constructLambdaChain(childSibling));
                break;
            default:
           // Node types correspond to various CSE (Common Subexpression Elimination) optimization rules and logical/arithmetical operations; we skip standardization for them because their semantics remain unchanged during the standardization process.
                break;
        }
    }

    private void populateCommaAndTauNode(ASTNode equalNode, ASTNode commaNode, ASTNode tauNode) {
        if (equalNode.getTypeOfASTNode() != ASTNodeType.EQUAL)
            throw new RuntimeException("SIMULTDEF: one of the children is not EQUAL"); //safety
        ASTNode x = equalNode.getChildOfASTNode();
        ASTNode e = x.getSiblingOfASTNode();
        setChild(commaNode, x);
        setChild(tauNode, e);
    }

    /**
     * Adds a new child to the parentNode. If the childNode is not null, it attaches the childNode as the last sibling
     * of the existing children of the parentNode.
     *
     * @param parentNode The parent node to attach to
     * @param childNode The new child node to add or attach
     */

    private void setChild(ASTNode parentNode, ASTNode childNode) {
        if (parentNode.getChildOfASTNode() == null)
            parentNode.setChildOfASTNode(childNode);
        else {
            ASTNode lastSibling = parentNode.getChildOfASTNode();
            while (lastSibling.getSiblingOfASTNode() != null)
                lastSibling = lastSibling.getSiblingOfASTNode();
            lastSibling.setSiblingOfASTNode(childNode);
        }
        childNode.setSiblingOfASTNode(null);
    }

    private ASTNode constructLambdaChain(ASTNode node) {
        if (node.getSiblingOfASTNode() == null)
            return node;

        ASTNode lambdaNode = new ASTNode();
        lambdaNode.setTypeOfASTNode(ASTNodeType.LAMBDA);
        lambdaNode.setChildOfASTNode(node);
        if (node.getSiblingOfASTNode().getSiblingOfASTNode() != null)
            node.setSiblingOfASTNode(constructLambdaChain(node.getSiblingOfASTNode()));
        return lambdaNode;
    }

    /**
     * Creates delta structures from the standardized tree
     *
     * @return the first delta structure (&delta;0)
     */
    public DeltaControlStructure createDeltas() {
        pending_Delta_Body_Queue = new ArrayDeque<PendingDeltaBody>();
        Delta_Index = 0;
        Cur_Delta = createDelta(root);
        processPendingDeltaStack();
        return Root_Delta;
    }

    private DeltaControlStructure createDelta(ASTNode startBodyNode) {
        //we'll create this delta's body later
        PendingDeltaBody pendingDelta = new PendingDeltaBody();
        pendingDelta.startNode = startBodyNode;
        pendingDelta.body = new Stack<ASTNode>();
        pending_Delta_Body_Queue.add(pendingDelta);

        DeltaControlStructure d = new DeltaControlStructure();
        d.setBody(pendingDelta.body);
        d.setIndex(Delta_Index++);
        Cur_Delta = d;

        if (startBodyNode == root)
            Root_Delta = Cur_Delta;

        return d;
    }

    private void processPendingDeltaStack() {
        while (!pending_Delta_Body_Queue.isEmpty()) {
            PendingDeltaBody pendingDeltaBody = pending_Delta_Body_Queue.pop();
            buildDeltaBody(pendingDeltaBody.startNode, pendingDeltaBody.body);
        }
    }

    private void buildDeltaBody(ASTNode node, Stack<ASTNode> body) {
        if (node.getTypeOfASTNode() == ASTNodeType.LAMBDA) { //create a new delta
            DeltaControlStructure d = createDelta(node.getChildOfASTNode().getSiblingOfASTNode());
            if (node.getChildOfASTNode().getTypeOfASTNode() == ASTNodeType.COMMA) { //the left child of the lambda is the bound variable
                ASTNode commaNode = node.getChildOfASTNode();
                ASTNode childNode = commaNode.getChildOfASTNode();
                while (childNode != null) {
                    d.addBoundVars(childNode.getValueOfASTNode());
                    childNode = childNode.getSiblingOfASTNode();
                }
            } else
                d.addBoundVars(node.getChildOfASTNode().getValueOfASTNode());
            body.push(d); //add this new delta to the existing delta's body
            return;
        } else if (node.getTypeOfASTNode() == ASTNodeType.CONDITIONAL) {
            //Enable programming order evaluation, traverse the children in reverse order so the condition leads

            ASTNode conditionNode = node.getChildOfASTNode();
            ASTNode thenNode = conditionNode.getSiblingOfASTNode();
            ASTNode elseNode = thenNode.getSiblingOfASTNode();

            //Add a Beta_Condition_Eval node.
            Beta_Condition_Eval betaNode = new Beta_Condition_Eval();

            buildDeltaBody(thenNode, betaNode.getThenBody());
            buildDeltaBody(elseNode, betaNode.getElseBody());

            body.push(betaNode);

            buildDeltaBody(conditionNode, body);

            return;
        }

        //Pre oder walk
        body.push(node);
        ASTNode childNode = node.getChildOfASTNode();
        while (childNode != null) {
            buildDeltaBody(childNode, body);
            childNode = childNode.getSiblingOfASTNode();
        }
    }

    public boolean isASTStandardized() {
        return Standardized;
    }

    private static class PendingDeltaBody {
        Stack<ASTNode> body;
        ASTNode startNode;
    }
}

