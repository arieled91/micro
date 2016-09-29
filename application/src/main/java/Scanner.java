import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class Scanner {

    NodeList<Character> chars;
    private final Map<String, TokenType> reservedWords = new HashMap<>();
    private final List<Token> tokens = new LinkedList<>();
    private final ListIterator<Token> tokensIterator = tokens.listIterator();

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
        char[] fileChars = FileUtil.read(filePath).toCharArray();
        chars = NodeList.fromCharArray(fileChars);
        loadReservedWords();
        buildTokens();
    }

    private void loadReservedWords(){
        for (final TokenType tokenType : TokenType.values()) {
            if(tokenType.isReservedWord())
                reservedWords.put(tokenType.value(), tokenType);
        }
    }

    @NotNull public Token next(){
        if(tokensIterator.hasNext()) return tokensIterator.next();
        else return new Token();
    }

    @NotNull public Token previous(){
        if(tokensIterator.hasPrevious())return tokensIterator.previous();
        else return new Token();
    }

    private void buildTokens(){
        String buffer = "";
        int status = 0;

        while (chars.next()!=null) {
            Character character = chars.get();
            status = nextStatus(status, character);
            if(isFinalStatus(status)) {
                if(buffer.length()==0) buffer+=character;
                else chars.previous();
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




    /*
    @NotNull private Token buildTokens(){
        String buffer = "";
        int status = 0;


        while(charsIterator.hasNext()){
            final Character character = charsIterator.next();
            final int nextStatus = nextStatus(status, character);
            if(nextStatus==14) break;
            status = nextStatus;
            buffer+=character;
        }

        final Token token = new Token(buffer);

        switch (status){
            case 2:
                final TokenType tokenType = reservedWords.get(buffer);
                if(tokenType!=null) return token.withType(tokenType);
                return token.withType(TokenType.ID);
            case 4:
                token.withType(TokenType.CONSTANT); break;
            case 5:
                token.withType(TokenType.ADD_OP); break;
            case 6:
                token.withType(TokenType.SUB_OP); break;
            case 7:
                token.withType(TokenType.LEFT_PARENT); break;
            case 8:
                token.withType(TokenType.RIGHT_PARENT); break;
            case 9:
                token.withType(TokenType.COMMA); break;
            case 10:
                token.withType(TokenType.SEMICOLON); break;
            case 12:
                token.withType(TokenType.ASSIGN); break;
            case 13:
                token.withType(TokenType.EOF); break;
            default:
                token.withType(TokenType.LEXICAL_ERROR); break;
        }
        return token;
    }
*/
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
}
