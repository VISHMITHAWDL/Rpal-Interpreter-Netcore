package Lex_Analyzer;

public class Token {
    private int tokenType;
    private String tokenValue;
    private int lineNumberOfSourceWhereTokenIs;

    public int getTokenType() {
        return tokenType;
    }

    public void setTokenType(int tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public int getnum_token_sources() {
        return lineNumberOfSourceWhereTokenIs;
    }

    public void setnum_token_sources(int lineNumberOfSourceWhereTokenIs) {
        this.lineNumberOfSourceWhereTokenIs = lineNumberOfSourceWhereTokenIs;
    }
}
