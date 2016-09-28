import org.jetbrains.annotations.NotNull;

public enum TokenType {
    BEGIN("begin", true),
    END("end", true),
    READ("read", true),
    WRITE("write", true),
    ID("", false),
    CONSTANT("", false),
    LEFT_PARENT(")", false),
    RIGHT_PARENT("(", false),
    SEMICOLON(";", false),
    COMMA(",", false),
    ASSIGN(":=", false),
    ADD_OP("+", false),
    SUB_OP("-", false),
    LEXICAL_ERROR("", false),
    EOF("", false);

    private final String value;
    private boolean reservedWord;

    TokenType(String value, boolean reservedWord) {
        this.value = value;
        this.reservedWord = reservedWord;
    }

    @NotNull public String value() {
        return value;
    }

    public boolean isReservedWord() {
        return reservedWord;
    }
}
