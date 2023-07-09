import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class File {
    public static String[] readFile(String path) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(path)); // Gets the content of file to the list.
            return lines.toArray(new String[0]);
        } catch (IOException e) { // Returns null if there is no such a file.
            e.printStackTrace();
            return null;
        }
    }

    public static void writeFile(String path, String content, boolean append, boolean newLine) {
        PrintStream ps = null;
        try {
            ps = new PrintStream(new FileOutputStream(path, append));
            ps.print(content + (newLine ? "\n" : ""));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ps != null) { // Flushes all the content and closes the stream if it has been successfully
                              // created.
                ps.flush();
                ps.close();
            }
        }
    }
}