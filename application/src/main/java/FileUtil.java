import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    @NotNull public static String read(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            System.err.println(String.format(FILE_NOT_FOUND,path));
            System.exit(5);
        }

        return "";
    }

    private static final String FILE_NOT_FOUND = "Error de E/S o archivo no encontrado: \"%s\"";

}
