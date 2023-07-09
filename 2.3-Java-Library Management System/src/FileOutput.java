import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class FileOutput {
	/**
	 * This function writes given content to file at given path.
	 *
	 * @param content Content that is going to be written to file.
	 */
	public static void print(String content) {
		PrintStream ps = null;
		try {
			ps = new PrintStream(new FileOutputStream(CommandManager.outputFile, true));
			ps.print(content + "\n");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			if (ps != null) { //Flushes all the content and closes the stream if it has been successfully created.
				ps.flush();
				ps.close();
			}
		}
	}
}