import org.jetbrains.annotations.NotNull;

public class Compiler {

    char[] chars = null;

    public static Compiler build(char... chars){
        Compiler compiler = new Compiler();
        compiler.setChars(chars);
        return compiler;
    }

    public void setChars(char[] chars) {
        this.chars = chars;
    }


    public void compile(){
        begin();
        match(Token.BEGIN);
        listOfStatements();
        match(Token.END);

    }


    private void listOfStatements() {
        statement();
        while (true){
            switch (nextToken()){
                case ID: case READ: case WRITE:
                    statement();
                    break;
                default:
                    return;
            }
        }
    }


    private void statement() {
        Token token = nextToken();
        
    }

    @NotNull private Token nextToken() {
        return null;
    }




    private void match(Token token) {

    }

    private void begin() {

    }
}
