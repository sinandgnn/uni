import java.io.File;

public class Main {
	public static void main(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];

		File f = new File(outputFile);
		if (f.exists())
			f.delete();

		String[] commands = FileInput.readFile(inputFile);
		CommandManager.run(commands, outputFile);
	}
}