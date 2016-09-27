import org.jetbrains.annotations.NotNull;

public enum Token {
    BEGIN("begin"),
    END("end"),
    READ("read"),
    WRITE("write"),
    ID(""),
    CONSTANT(""),
    LEFT_PARENT(")"),
    RIGHT_PARENT("("),
    SEMICOLON(";"),
    COMMA(","),
    ASSIGN("="),
    ADD_OP("+"),
    SUB_OP("-"),
    LEXICAL_ERROR("");

    private final String value;

    Token(String value) {
        this.value = value;
    }

    @NotNull public String value() {
        return value;
    }
}
