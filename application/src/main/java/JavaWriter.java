import utils.NodeList;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class JavaWriter {
    private String originPath;
    private NodeList<Token> tokens;

    public JavaWriter() {}

    public JavaWriter(String originPath, NodeList<Token> tokens) {
        this.originPath = originPath;
        this.tokens = tokens;
        this.tokens.resetIndex();
    }

    public void setOriginPath(String originPath) {
        this.originPath = originPath;
    }

    public void setTokens(NodeList<Token> tokens) {
        this.tokens = tokens;
    }

    public void write(){
        String targetPath = buildClassName(originPath, true);
        List<String> file = buildFile();
        try {
            Files.write(Paths.get(targetPath), file, Charset.forName("UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String buildClassName(String originPath, boolean fullPath){
        String[] pathSplit = originPath.split(File.separator);
        String name = pathSplit[pathSplit.length - 1].replace(".m","");
        String camelcase = name.substring(0, 1).toUpperCase() + name.substring(1); //to camelcase
        if(fullPath){
            pathSplit[pathSplit.length - 1] = camelcase;
            String newPath = "";
            for (String split : pathSplit) {
                newPath+=split;
            }
            return newPath+".java";
        }
        return camelcase;
    }

    private List<String> buildFile(){
        List<String> file = new ArrayList<>();
        while (tokens.next()!=null) {
            Token token = tokens.get();
            if(token==null) break;
            TokenType tokenType = token.getTokenType();
            final Set<String> idMap = new HashSet<>();

            switch (tokenType){
                case BEGIN:
                    file.add("import java.util.Scanner;\n");
                    file.add(String.format("public class %s {", buildClassName(originPath, false)));
                    file.add("\tpublic static void main(String[] args) {");
                    file.add("\t\tScanner read = new Scanner(System.in);");
                    break;

                case READ:
                    tokens.next(); //skip read token
                    do {
                        Token id = tokens.next();
                        if (id == null) break;
                        idMap.add(id.getValue());
                        file.add(String.format("\t\tSystem.out.println(\"Ingrese %s: \");", id.getValue()));
                        file.add(String.format("\t\tint %s = read.nextInt();", id.getValue()));
                    }while (tokens.next().getTokenType()!=TokenType.RIGHT_PARENT);
                    tokens.next(); //semicolon
                    break;

                case WRITE:
                    tokens.next(); //skip write token
                    while (tokens.next().getTokenType()!=TokenType.SEMICOLON){
                        String expBuffer = "";
                        while (tokens.get().getTokenType()!=TokenType.COMMA && tokens.get().getTokenType()!=TokenType.RIGHT_PARENT){
                            Token expression = tokens.get();
                            if (expression == null) break;
                            expBuffer+=expression.getValue();
                            tokens.next();
                        }
                        file.add(String.format("\t\tSystem.out.println(%s);",expBuffer));
                    }
                    break;

                case ID:
                    String stateBuffer = "\t\t";
                    Token id = tokens.get();
                    if (id == null) break;
                    if(!idMap.contains(id.getValue()))
                        stateBuffer+="int ";
                    stateBuffer+=id.getValue() + " = ";
                    tokens.next(); //skip :=
                    while (tokens.next().getTokenType()!=TokenType.SEMICOLON){
                        stateBuffer+=tokens.get().getValue();
                    }
                    file.add(stateBuffer+";");
                    break;

                case END:
                    file.add("\t}");
                    file.add("}");
                    break;
            }
        }
        return file;
    }
}
