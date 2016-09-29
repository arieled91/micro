public class Token {
    private TokenType tokenType = TokenType.OTHER;
    private String value = "";

    public Token() {}

    public Token(String value) {
        this.value = value;
    }

    public TokenType getTokenType() {
        return tokenType;
    }

    public Token withType(TokenType type) {
        tokenType = type;
        return this;
    }

    public String getValue() {
        return value;
    }


    public void syntacticError(){
        System.err.println(String.format(SYNTACTIC_ERROR, getValue()));
    }

    public void lexicalError(){
        System.err.println(String.format(LEXICAL_ERROR, getValue()));
    }



    private static final String SYNTACTIC_ERROR = "Error sintáctico: %s";
    private static final String LEXICAL_ERROR = "Error léxico: %s";
}
