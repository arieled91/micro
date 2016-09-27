import org.jetbrains.annotations.NotNull;

public class App {

    public static void main(String... args) {
        if(args.length==0) {
            System.err.println(FILE_PATH_ERROR);
            return;
        }

        if(args.length>1) {
            System.err.println(PARAM_NUMBER_ERROR);
            return;
        }

        String filePath = args[0];

        if(!filePath.endsWith(".m")) {
            System.err.println(FILE_NAME_ERROR);
            return;
        }


        char[] program = FileUtil.read(filePath);
        Compiler.build(program).compile();
    }

    private static final String FILE_PATH_ERROR = "Debe ingresar la ruta de un archivo";
    private static final String PARAM_NUMBER_ERROR = "Número incorrecto de parámetros";
    private static final String FILE_NAME_ERROR = "Nombre incorrecto de archivo";
}
