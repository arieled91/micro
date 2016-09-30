import java.util.HashMap;
import java.util.Map;

public class Parser {

    private final Scanner scanner;
    private final Map<String, String> idMap = new HashMap<>();
    private final String filePath;

    public Parser (String filePath){
        this.filePath = filePath;
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
        writeJavaClass();
        return this;
    }

    private void writeJavaClass() {
        new JavaWriter(filePath, scanner.getTokens()).write();
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
        scanner.next();
        match(TokenType.ID);
        declareId();
        while(true){
            final Token token = scanner.next();
            if(token.getTokenType()!=TokenType.COMMA){
                scanner.previous();
                return;
            }
            scanner.next();
            match(TokenType.ID);
            declareId();
        }
    }


    private void statement() {
        final TokenType tokenType = scanner.next().getTokenType();
        switch (tokenType){
            case ID:
                declareId();
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
            case ID:
                checkId();
                break;
            case CONSTANT:
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

    private void declareId(){
        Token token = scanner.get();
        if(token.getTokenType()==TokenType.ID){
            idMap.put(token.getValue(), token.getValue());
        }
    }

    private void checkId(){
        Token token = scanner.get();
        if(!idMap.containsKey(token.getValue())){
            System.err.printf(ID_ERROR, token.getValue());
            System.exit(1);
        }
    }

    private static final String PARSER_OK = "No se detectaron errores sintacticos";
    private static final String ID_ERROR = "El identificador \"%s\" no fue declarado\n";

}
