import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import utils.FileUtil;
import utils.NodeList;

import java.util.HashMap;
import java.util.Map;

public class Scanner {

    NodeList<Character> chars;
    private final Map<String, TokenType> reservedWords = new HashMap<>();
    private final NodeList<Token> tokens = new NodeList<>();

    private final int[][] statusTable =
            {{ 1,  3,  5,  6,  7,  8,  9, 10, 11, 14, 13,  0, 14 },
            {  1,  1,  2,  2,  2,  2,  2,  2,  2,  2,  2,  2, 2  },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            {  4,  3,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4,  4 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 12, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 },
            { 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14, 14 }};

    public Scanner(String filePath) {
        loadChars(filePath);
        loadReservedWords();
        buildTokens();
        printResult();
    }

    private void loadChars(String filePath) {
        char[] fileChars = FileUtil.read(filePath).toCharArray();
        chars = NodeList.fromCharArray(fileChars);
    }

    private void printResult() {
        int errorCounter = 0;
        for (Token token : tokens.list()) {
            if(token.getTokenType()==TokenType.LEXICAL_ERROR) {
                token.lexicalError();
                errorCounter++;
            }
        }

        if(errorCounter==0) System.out.println(SCANNER_OK);
        else System.err.println(String.format(SCANNER_ERROR, errorCounter));

    }

    private void loadReservedWords(){
        for (final TokenType tokenType : TokenType.values()) {
            if(tokenType.isReservedWord())
                reservedWords.put(tokenType.value(), tokenType);
        }
    }

    @NotNull public Token next(){
        Token next = tokens.next();
        return next != null ? next : new Token();
    }

    @NotNull public Token previous(){
        Token previous = tokens.previous();
        return previous!=null ? previous : new Token();
    }

    @NotNull public Token get(){
        Token get = tokens.get();
        return get!=null ? get : new Token();
    }

    private void buildTokens(){
        String buffer = "";
        int status = 0;

        while (chars.next()!=null) {
            Character character = chars.get();
            status = nextStatus(status, character);
            if(isFinalStatus(status)) {
                if(status==2 || status==4)chars.previous();
                else buffer+=character;
                tokens.add(buildToken(status, buffer));
                buffer = "";
                status = 0;
            }else {
                if (!isIgnoredChar(character)) buffer += character;
            }
        }
    }

    private boolean isFinalStatus(int s){
        return s==2 || s==4 || s==5 || s==6 || s==7 || s==8 || s==9 || s==10 || s==12 || s==13 || s==14;
    }

    private Token buildToken(int status, String buffer){
        final Token token = new Token(buffer);
        switch (status) {
            case 2:
                final TokenType tokenType = reservedWords.get(buffer);
                if (tokenType != null) token.withType(tokenType);
                else token.withType(TokenType.ID);
                break;
            case 4:
                token.withType(TokenType.CONSTANT);
                break;
            case 5:
                token.withType(TokenType.ADD_OP);
                break;
            case 6:
                token.withType(TokenType.SUB_OP);
                break;
            case 7:
                token.withType(TokenType.LEFT_PARENT);
                break;
            case 8:
                token.withType(TokenType.RIGHT_PARENT);
                break;
            case 9:
                token.withType(TokenType.COMMA);
                break;
            case 10:
                token.withType(TokenType.SEMICOLON);
                break;
            case 12:
                token.withType(TokenType.ASSIGN);
                break;
            case 13:
                token.withType(TokenType.EOF);
                break;
            default:
                token.withType(TokenType.LEXICAL_ERROR);
                break;
        }
        return token;
    }

    private int nextStatus(int status, @Nullable Character character){
        final int column;
        if(character==null) column = 12;
        else if(character=='\0') column = 10;
        else if(Character.isAlphabetic(character)) column = 0;
        else if(Character.isDigit(character)) column = 1;
        else if(character == '+') column = 2;
        else if(character == '-') column = 3;
        else if(character == '(') column = 4;
        else if(character == ')') column = 5;
        else if(character == ',') column = 6;
        else if(character == ';') column = 7;
        else if(character == ':') column = 8;
        else if(character == '=') column = 9;
        else if(isIgnoredChar(character)) column = 11;
        else column = 12;
        return statusTable[status][column];
    }

    private boolean isIgnoredChar(Character c){
        return c != null && (c==' ' || c=='\t' || c=='\n');
    }

    private static final String SCANNER_OK = "No se detectaron erores léxicos";
    private static final String SCANNER_ERROR = "Se detectaron errores léxicos: %s";
}
