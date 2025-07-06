package Lex_Analyzer;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Scanner {

    // Declaration of constant integer values for each possible token type

    public static final int TOKEN_TYPE_IDENTIFIER = 0;
    public static final int TOKEN_TYPE_INTEGER = 1;
    public static final int TOKEN_TYPE_STRING = 2;
    public static final int TOKEN_TYPE_OPERATOR = 3;
    public static final int TOKEN_TYPE_DELETE = 4;
    public static final int TOKEN_TYPE_L_PAREN = 5;
    public static final int TOKEN_TYPE_R_PAREN = 6;
    public static final int TOKEN_TYPE_SEMICOLON = 7;
    public static final int TOKEN_TYPE_COMMA = 8;
    // Reserved Tokens are keywords such as 'let, 'aug', 'within', 'and', ...
    public static final int TOKEN_TYPE_RESERVED = 9;

    //Declaration of regular expressions for token matching

    private static final String letterRegularExpressionMatchString = "a-zA-Z";
    public static final Pattern LetterRegularExpression = Pattern.compile("[" + letterRegularExpressionMatchString + "]");
    private static final String digitRegularExpressionMatchString = "\\d";
    public static final Pattern IdentifierRegularExpression = Pattern.compile("[" + letterRegularExpressionMatchString + digitRegularExpressionMatchString + "_]");
    public static final Pattern DigitRegularExpression = Pattern.compile(digitRegularExpressionMatchString);
    private static final String spaceRegularExpressionMatchString = "[\\s\\t\\n]";

    // Regular Expression
    public static final Pattern SpaceRegularExpression = Pattern.compile(spaceRegularExpressionMatchString);
    private static final String punctuationRegularExpressionMatchString = "();,";
    public static final Pattern PunctuationRegularExpression = Pattern.compile("[" + punctuationRegularExpressionMatchString + "]");
    private static final String operatorSymbolRegularExpressionMatchString = "+-/~:=|!#%_{}\"*<>.&$^\\[\\]?@";
    private static final String operatorSymbolToEscapeRegularExpressionMatchString = "([*<>.&$^?])";
    public static final Pattern OperatorSymbolRegularExpression = Pattern.compile("[" + escapeMetaChars() + "]");
    public static final Pattern StringRegularExpression = Pattern.compile("[ \\t\\n\\\\" + punctuationRegularExpressionMatchString + letterRegularExpressionMatchString + digitRegularExpressionMatchString + escapeMetaChars() + "]");
    public static final Pattern CommentRegularExpression = Pattern.compile("[ \\t\\'\\\\ \\r" + punctuationRegularExpressionMatchString + letterRegularExpressionMatchString + digitRegularExpressionMatchString + escapeMetaChars() + "]"); //the \\r is for Windows LF; not really required since we're targeting *nix systems
    private final List<String> reservedIdentifiers = Arrays.asList("let", "in", "within", "fn", "where", "aug", "or",
            "not", "gr", "ge", "ls", "le", "eq", "ne", "true",
            "false", "nil", "dummy", "rec", "and");

    private final BufferedReader buffer;
    private String extraCharRead;
    private int currentLineNumberInRPALSource;

    public Scanner(String inputFile) throws IOException {
        currentLineNumberInRPALSource = 1;
        buffer = new BufferedReader(new InputStreamReader(new FileInputStream(new File(inputFile))));
    }

    private static String escapeMetaChars() {
        return Scanner.operatorSymbolRegularExpressionMatchString.replaceAll(Scanner.operatorSymbolToEscapeRegularExpressionMatchString, "\\\\\\\\$1");
    }

    /**
     * Returns next token from the RPAL source file
     *
     * @return null if the file has ended
     */
    public Token readNextToken() {
        Token nextToken = null;
        String nextChar;
        if (extraCharRead != null) {
            nextChar = extraCharRead;
            extraCharRead = null;
        } else
            nextChar = getNextCharacterFromSource();
        if (nextChar != null)
            nextToken = matchAndGetNextToken(nextChar);
        return nextToken;
    }

    /**
     * Returns next character from the RPAL source file
     * if the next character is '\n', increment the currentLineNumberInRPALSource by 1
     *
     * @return null if the file has ended
     */
    private String getNextCharacterFromSource() {
        String nextChar = null;
        try {
            int c = buffer.read();
            if (c != -1) {
                nextChar = Character.toString((char) c);
                if (nextChar.equals("\n")) currentLineNumberInRPALSource++;
            } else
                buffer.close();
        } catch (IOException e) {
        }
        return nextChar;
    }

    /**
     * gets next token from the RPAL source file
     *
     * @param currentCharacter character currently being processed
     * @return token that was built
     */
    private Token matchAndGetNextToken(String currentCharacter) {
        Token nextToken = null;
        if (LetterRegularExpression.matcher(currentCharacter).matches()) {
            nextToken = getIdentifierToken(currentCharacter);
        } else if (DigitRegularExpression.matcher(currentCharacter).matches()) {
            nextToken = getIntegerToken(currentCharacter);
        } else if (OperatorSymbolRegularExpression.matcher(currentCharacter).matches()) {
            nextToken = getOperatorToken(currentCharacter);
        } else if (currentCharacter.equals("'")) {
            nextToken = getStringToken();
        } else if (SpaceRegularExpression.matcher(currentCharacter).matches()) {
            nextToken = getSpaceToken(currentCharacter);
        } else if (PunctuationRegularExpression.matcher(currentCharacter).matches()) {
            nextToken = getPunctuationToken(currentCharacter);
        }
        return nextToken;
    }

    /**
     * Get a Identifier token.
     * Identifier -> Letter (Letter | Digit | '_')*
     *
     * @param currentCharacter character currently being processed
     * @return token that was built
     */
    private Token getIdentifierToken(String currentCharacter) {
        Token identifierToken = new Token();
        identifierToken.setTokenType(TOKEN_TYPE_IDENTIFIER);
        identifierToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder(currentCharacter);

        String nextChar = getNextCharacterFromSource();
        while (nextChar != null) { //null indicates the file ended
            if (IdentifierRegularExpression.matcher(nextChar).matches()) {
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            } else {
                extraCharRead = nextChar;
                break;
            }
        }

        String value = sBuilder.toString();
        if (reservedIdentifiers.contains(value))
            identifierToken.setTokenType(TOKEN_TYPE_RESERVED);

        identifierToken.setTokenValue(value);
        return identifierToken;
    }

    /**
     * Get an integer token.
     * Integer -> Digit+
     *
     * @param currentCharacter character currently being processed
     * @return token that was built
     */
    private Token getIntegerToken(String currentCharacter) {
        Token integerToken = new Token();
        integerToken.setTokenType(TOKEN_TYPE_INTEGER);
        integerToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder(currentCharacter);

        String nextChar = getNextCharacterFromSource();
        while (nextChar != null) { //null indicates the file ended
            if (DigitRegularExpression.matcher(nextChar).matches()) {
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            } else {
                extraCharRead = nextChar;
                break;
            }
        }

        integerToken.setTokenValue(sBuilder.toString());
        return integerToken;
    }

    /**
     * Get an operator token.
     * Operator -> Operator_symbol+
     *
     * @param currentCharacter character currently being processed
     * @return token that was built
     */
    private Token getOperatorToken(String currentCharacter) {
        Token operatorSymbolToken = new Token();
        operatorSymbolToken.setTokenType(TOKEN_TYPE_OPERATOR);
        operatorSymbolToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder(currentCharacter);

        String nextChar = getNextCharacterFromSource();

        if (currentCharacter.equals("/") && nextChar.equals("/"))
            return getCommentToken(currentCharacter + nextChar);

        while (nextChar != null) { //null indicates the file ended
            if (OperatorSymbolRegularExpression.matcher(nextChar).matches()) {
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            } else {
                extraCharRead = nextChar;
                break;
            }
        }

        operatorSymbolToken.setTokenValue(sBuilder.toString());
        return operatorSymbolToken;
    }

    /**
     * Get a string token.
     * String -> '''' ('\' 't' | '\' 'n' | '\' '\' | '\' '''' |'(' | ')' | ';' | ',' |'' |Letter | Digit | Operator_symbol )* ''''
     *
     * @return token that was built
     */
    private Token getStringToken() {
        Token stringToken = new Token();
        stringToken.setTokenType(TOKEN_TYPE_STRING);
        stringToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder();

        String nextChar = getNextCharacterFromSource();
        while (nextChar != null) { //null indicates the file ended
            if (nextChar.equals("'")) { //we just used up the last char we read, hence no need to set extraCharRead
                //sBuilder.append(nextChar);
                stringToken.setTokenValue(sBuilder.toString());
                return stringToken;
            } else if (StringRegularExpression.matcher(nextChar).matches()) { //match Letter | Digit | Operator_symbol
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            }
        }

        return null;
    }

    /**
     * Get a string token and set the type of the token to be deleted
     * Spaces -> ( ’ ’ | ht | Eol )+
     *
     * @return a delete token
     */
    private Token getSpaceToken(String currentCharacter) {
        Token spaceDeleteToken = new Token();
        spaceDeleteToken.setTokenType(TOKEN_TYPE_DELETE);
        spaceDeleteToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder(currentCharacter);

        String nextChar = getNextCharacterFromSource();
        while (nextChar != null) { //null indicates the file ended
            if (SpaceRegularExpression.matcher(nextChar).matches()) {
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            } else {
                extraCharRead = nextChar;
                break;
            }
        }

        spaceDeleteToken.setTokenValue(sBuilder.toString());
        return spaceDeleteToken;
    }

    /**
     * Get a comment token and set the type of the token to be deleted
     * Comment -> ’//’( ’’’’ | ’(’ | ’)’ | ’;’ | ’,’ | ’\’ | ’ ’ | ht | Letter | Digit | Operator_symbol )* Eol
     *
     * @return a delete token
     */
    private Token getCommentToken(String currentCharacter) {
        Token commentDeleteToken = new Token();
        commentDeleteToken.setTokenType(TOKEN_TYPE_DELETE);
        commentDeleteToken.setnum_token_sources(currentLineNumberInRPALSource);
        StringBuilder sBuilder = new StringBuilder(currentCharacter);

        String nextChar = getNextCharacterFromSource();
        while (nextChar != null) { //null indicates the file ended
            if (CommentRegularExpression.matcher(nextChar).matches()) {
                sBuilder.append(nextChar);
                nextChar = getNextCharacterFromSource();
            } else if (nextChar.equals("\n"))
                break;
        }
        commentDeleteToken.setTokenValue(sBuilder.toString());
        return commentDeleteToken;
    }

    /**
     * Gets punctuation tokens
     * Punction ->’(’
     * -> ’)’
     * -> ’;’
     * -> ’,’
     *
     * @return token that was built
     */
    private Token getPunctuationToken(String currentCharacter) {
        Token punctuationToken = new Token();
        punctuationToken.setnum_token_sources(currentLineNumberInRPALSource);
        punctuationToken.setTokenValue(currentCharacter);
        switch (currentCharacter) {
            case "(" -> punctuationToken.setTokenType(TOKEN_TYPE_L_PAREN);
            case ")" -> punctuationToken.setTokenType(TOKEN_TYPE_R_PAREN);
            case ";" -> punctuationToken.setTokenType(TOKEN_TYPE_SEMICOLON);
            case "," -> punctuationToken.setTokenType(TOKEN_TYPE_COMMA);
        }
        return punctuationToken;
    }
}

