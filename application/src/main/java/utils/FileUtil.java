package utils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {

    private FileUtil() {}

    @NotNull public static String read(String path){
        try {
            return new String(Files.readAllBytes(Paths.get(path)));
        } catch (final IOException e) {
            System.err.println(String.format(FILE_NOT_FOUND,path));
            System.exit(5);
        }

        return "";
    }

    public static void write(String targetPath, Iterable<String> file){
        try {
            Files.write(Paths.get(targetPath), file, Charset.forName("UTF-8"));
        } catch (IOException e) {
            System.err.println(String.format(FILE_NOT_FOUND,targetPath));
            System.exit(5);
        }
    }

    private static final String FILE_NOT_FOUND = "Error de E/S o recurso no encontrado: \"%s\"";
}
