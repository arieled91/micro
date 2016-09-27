import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    @NotNull public static char[] read(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path))).toCharArray();
        } catch (IOException e) {
            System.err.println(String.format(FILE_NOT_FOUND,path));
        }

        return "".toCharArray();
    }

    private static final String FILE_NOT_FOUND = "No se encuentra el archivo \"%s\"";

}
