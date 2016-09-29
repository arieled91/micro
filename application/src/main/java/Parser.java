public class Parser {

    private final Scanner scanner;


    public Parser (String filePath){
        scanner = new Scanner(filePath);
    }

    public Parser run(){
        scanner.next();
        match(TokenType.BEGIN);
        listOfStatements();
        scanner.next();
        match(TokenType.END);
        scanner.next();
        match(TokenType.EOF);
        System.out.println(PARSER_OK);
        return this;
    }


    public boolean match(TokenType matchType){
        Token token = scanner.get();
        if(token.getTokenType()!=matchType) {
            token.syntacticError();
            System.exit(1);
            return false;
        }
        return true;
    }

    private void listOfStatements() {
        statement();
        while (true){
            final TokenType tokenType = scanner.next().getTokenType();
            switch (tokenType){
                case ID: case READ: case WRITE:
                    scanner.previous();
                    statement();
                    break;
                default:
                    scanner.previous();
                    return;
            }
        }
    }

    private void listOfExpressions() {
        expression();
        while(true){
            final Token token = scanner.next();
            if(token.getTokenType()!=TokenType.COMMA){
                scanner.previous();
                return;
            }
            expression();
        }
    }

    private void listOfIdentifiers(){
        identifier();
        while(true){
            final Token token = scanner.next();
            if(token.getTokenType()!=TokenType.COMMA){
                scanner.previous();
                return;
            }
            identifier();
        }
    }

    private void statement() {
        final TokenType tokenType = scanner.next().getTokenType();
        switch (tokenType){
            case ID:
                scanner.next();
                match(TokenType.ASSIGN);
                expression();
                break;
            case READ:
                scanner.next();
                match(TokenType.LEFT_PARENT);
                listOfIdentifiers();
                scanner.next();
                match(TokenType.RIGHT_PARENT);
                break;
            case WRITE:
                scanner.next();
                match(TokenType.LEFT_PARENT);
                listOfExpressions();
                scanner.next();
                match(TokenType.RIGHT_PARENT);
                break;
            default: break;
        }
        scanner.next();
        match(TokenType.SEMICOLON);
    }

    private void expression(){
        primary();
        while(true){
            final Token token = scanner.next();
            if(token.getTokenType()!=TokenType.ADD_OP && token.getTokenType()!=TokenType.SUB_OP){
                scanner.previous();
                return;
            }
            primary();
        }
    }

    private void primary(){
        final Token token = scanner.next();
        final TokenType tokenType = token.getTokenType();

        switch (tokenType){
            case ID: case CONSTANT:
                break;
            case LEFT_PARENT:
                expression();
                scanner.next();
                match(TokenType.RIGHT_PARENT);
                break;
            default:
                token.syntacticError();
                break;
        }
    }

    private void identifier(){
        scanner.next();
        match(TokenType.ID);
        //procesar id
    }

    private static final String PARSER_OK = "No se detectaron errores sintacticos";
}
