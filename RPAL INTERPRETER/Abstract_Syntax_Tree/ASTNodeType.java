package Abstract_Syntax_Tree;

public enum ASTNodeType {
    //Expression
    LET("let"),
    LAMBDA("lambda"),
    WHERE("where"),

    //Tuple Expression
    TAU("tau"),
    AUG("aug"),
    CONDITIONAL("->"),

    //General
    IDENTIFIER("<ID:%s>"),
    STRING("<STR:'%s'>"),
    INTEGER("<INT:%s>"),

    //Arithmetic Expressions
    PLUS("+"),
    MINUS("-"),
    NEG("neg"),
    MULT("*"),
    DIV("/"),
    EXP("**"),
    AT("@"),

    //Rators and Rands
    GAMMA("gamma"),
    TRUE("<true>"),
    FALSE("<false>"),
    NIL("<nil>"),
    DUMMY("<dummy>"),

    //Definitions
    WITHIN("within"),
    SIMULTDEF("and"),
    REC("rec"),
    EQUAL("="),
    FCNFORM("function_form"),

    //Varibles
    PAREN("<()>"),
    COMMA(","),

    // Boll expression
    OR("or"),
    AND("&"),
    NOT("not"),
    GR("gr"),
    GE("ge"),
    LS("ls"),
    LE("le"),
    EQ("eq"),
    NE("ne"),

    // Ystar
    YSTAR("<Y*>"),

    // programee evolution
    BETA(""),
    DELTA(""),
    ETA(""),
    TUPLE("");

    private final String printNameOfASTNode; //used for printing AST representation

    ASTNodeType(String name) {
        printNameOfASTNode = name;
    }

    public String getPrintNameOfASTNode() {
        return printNameOfASTNode;
    }
}
