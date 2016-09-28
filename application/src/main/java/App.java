public class App {

    private App() {}

    public static void main(String... args) {
        if(args.length==0) {
            System.err.println(FILE_PATH_ERROR);
            System.exit(1);
        }

        if(args.length>1) {
            System.err.println(PARAM_NUMBER_ERROR);
            System.exit(7);
        }

        final String filePath = args[0];

        if(!filePath.endsWith(".m")) {
            System.err.println(FILE_NAME_ERROR);
            System.exit(22);
        }



        final Parser parser = new Parser(filePath).run();
        //if(parser.isError()) System.exit(1);
    }

    private static final String FILE_PATH_ERROR = "Debe ingresar la ruta de un archivo";
    private static final String PARAM_NUMBER_ERROR = "Número incorrecto de parámetros";
    private static final String FILE_NAME_ERROR = "Nombre incorrecto de archivo";
}
