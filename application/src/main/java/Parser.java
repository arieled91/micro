public class Parser {

    private final Scanner scanner;
    private boolean error = false;


    public Parser (String buffer){
        scanner = new Scanner(buffer);
    }

    public Parser run(){
        match(TokenType.BEGIN);
        listOfStatements();
        match(TokenType.END);
        return this;
    }



    private void listOfStatements() {
        statement();
        /*while (true){
            switch (nextToken()){
                case ID: case READ: case WRITE:
                    statement();
                    break;
                default:
                    return;
            }
        }*/
    }


    private void statement() {
//        TokenType tokenType = nextToken();

    }



    private void match(TokenType tokenType) {
        Token token = scanner.next();
        if(token.getTokenType()!=tokenType) {
            System.err.println(String.format(SYNTACTIC_ERROR, token.getValue()));
            error = true;
        }
    }


    private void begin() {

    }

    public boolean isError() {
        return error;
    }

    private static final String SYNTACTIC_ERROR = "Error sintáctico: %s";
}
