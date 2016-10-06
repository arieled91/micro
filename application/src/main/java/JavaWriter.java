import utils.FileUtil;
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
        FileUtil.write(targetPath,file);
    }

    public String buildClassName(String originPath, boolean fullPath){
        String[] pathSplit = originPath.split(File.separator);
        String file = pathSplit[pathSplit.length - 1];
        String name = file.replace(".m","");
        String camelcase = name.substring(0, 1).toUpperCase() + name.substring(1); //to camelcase
        if(fullPath){
            pathSplit[pathSplit.length - 1] = camelcase;
            return originPath.substring(0,originPath.length()-file.length())+camelcase+".java";
        }
        return camelcase;
    }

    private List<String> buildFile(){
        List<String> file = new ArrayList<>();
        final Set<String> idMap = new HashSet<>();
        while (tokens.next()!=null) {
            Token token = tokens.get();
            if(token==null) break;
            TokenType tokenType = token.getTokenType();

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
                        file.add(String.format("\t\tSystem.out.println(\"Ingrese %s: \");", id.getValue()));
                        String declare = idMap.contains(id.getValue()) ? "" : "int ";
                        file.add(String.format("\t\t"+declare+"%s = read.nextInt();", id.getValue()));
                        idMap.add(id.getValue());
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
                    if(!idMap.contains(id.getValue())) {
                        stateBuffer += "int ";
                        idMap.add(id.getValue());
                    }
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
