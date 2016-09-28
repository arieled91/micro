public class Token {
    private TokenType tokenType;
    private String value;

    public Token(String value) {
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Token withTokenType(TokenType tokenType) {
        this.tokenType = tokenType;
        return this;
    }

    public String getValue() {
        return value;
    }
}
