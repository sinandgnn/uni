import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileInput {
	/**
	 * Reads the file at the given path and returns contents of it in a string array.
	 *
	 * @param path Path to the file that is going to be read.
	 * @return Contents of the file as a string array, returns null if there is not such a file or this program does not have sufficient permissions to read that file.
	 */
	public static String[] readFile(String path) {
		try {
			List<String> lines = Files.readAllLines(Paths.get(path)); //Gets the content of file to the list.
			return lines.toArray(new String[0]);
		} catch (IOException e) { //Returns null if there is no such a file.
			return new String[]{};
		}
	}
}