import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class Scanner {

    private String fileBuffer = "";
    private int bufferIndex = 0;
    private Map<String, TokenType> reservedWords = new HashMap<>();

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

    public Scanner(String fileBuffer) {
        this.fileBuffer = fileBuffer;
        loadReservedWords();
    }

    private void loadReservedWords(){
        for (TokenType tokenType : TokenType.values()) {
            if(tokenType.isReservedWord())
                reservedWords.put(tokenType.value(), tokenType);
        }
    }


    @Nullable private Character nextValue(){
        if(bufferIndex+1> fileBuffer.length()) return null;
        return fileBuffer.charAt(bufferIndex++);
    }

    private boolean isNextStatusError(int status){
        if(bufferIndex+1> fileBuffer.length()) return true;
        char value = fileBuffer.charAt(bufferIndex + 1);
        return nextStatus(status, value)!=14;
    }

    /*private boolean isNotFinalStatus(int s){
        return s==0 || s==1 || s==3 || s==11 || s==14;
    }*/



    @NotNull public Token next(){
        String buffer = "";
        int status = 0;

        while(isNextStatusError(status)){
            final Character character = nextValue();
            status = nextStatus(status, character);
            buffer+=character;
        }

        Token token = new Token(buffer);

        switch (status){
            case 2:
                TokenType tokenType = reservedWords.get(buffer);
                if(tokenType!=null) return token.withTokenType(tokenType);
                return token.withTokenType(TokenType.ID);
            case 4: return token.withTokenType(TokenType.CONSTANT);
            case 5: return token.withTokenType(TokenType.ADD_OP);
            case 6: return token.withTokenType(TokenType.SUB_OP);
            case 7: return token.withTokenType(TokenType.LEFT_PARENT);
            case 8: return token.withTokenType(TokenType.RIGHT_PARENT);
            case 9: return token.withTokenType(TokenType.COMMA);
            case 10: return token.withTokenType(TokenType.SEMICOLON);
            case 12: return token.withTokenType(TokenType.ASSIGN);
            case 13: return token.withTokenType(TokenType.EOF);
            default: return token.withTokenType(TokenType.LEXICAL_ERROR);
        }
    }

    private int nextStatus(int status, @Nullable Character character){
        int column;
        if(character==null) column = 10;
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
        else if(character == ' ' || character== '\t' || character== '\n') column = 11;
        else column = 12;
        return statusTable[status][column];
    }




}
