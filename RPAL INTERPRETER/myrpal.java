import Lex_Analyzer.Scanner;
import Parser.Parser;
import Abstract_Syntax_Tree.AST;
import Cse_Machine.CSE_Machine;

import java.io.IOException;

public class myrpal {
    public static void main(String[] args) {
        boolean printASTOnly = false;
        String inputFileName;

        // Handle the optional -ast switch
        if (args.length == 2 && args[0].equals("-ast")) {
            printASTOnly = true;
            inputFileName = args[1];
        }
        // Normal mode: just one argument (the RPAL file)
        else if (args.length == 1) {
            inputFileName = args[0];
        }
        // Bad usage
        else {
            System.out.println("Usage: java myrpal <input_file.rpal> / Usage: java myrpal -ast <input_file.rpal>");
            return;
        }

        // Build the AST
        AST astRoot = createAST(inputFileName);
        if (astRoot == null) return;

        // If -ast was specified, print the AST and exit
        if (printASTOnly) {
            astRoot.printAST();   // <— your new tree‐printing method
            return;
        }

        // Otherwise standardize and evaluate
        astRoot.Standardize();
        String result = interpretAST(astRoot);
        System.out.println(result);
    }

    private static AST createAST(String filePath) {
        try {
            Scanner scanner = new Scanner(filePath);
            Parser  parser  = new Parser(scanner);
            return parser.Build_AST();
        } catch (IOException ex) {
            System.err.println("ERROR: Cannot read file \"" + filePath + "\"");
            return null;
        }
    }

    private static String interpretAST(AST ast) {
        CSE_Machine machine = new CSE_Machine(ast);
        machine.evaluateRPALProgram();
        return machine.evaluationResult;
    }
}
