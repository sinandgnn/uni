public class Main {

	/**
	 * @param args An array containing the input file's name and output file's name.
	 */
	public static void main(String[] args) {
		String inputFile = args[0];
		String outputFile = args[1];

		FileOutput.writeToFile(outputFile, "", false, false); //Cleans the output file.
		String[] commands = FileInput.readFile(inputFile, true, true);

		if (commands != null && commands.length != 0) {
			String firstCommand = commands[0].split("\t")[0]; //First command.
			String lastCommand = commands[commands.length - 1]; //Last command.
			if (firstCommand.equals("SetInitialTime") && commands[0].split("\t").length == 2) {
				CommandManager.run(commands, outputFile);

				if (!lastCommand.equals("ZReport") && CommandManager.isRun) { //Checks the last command, prints report if not "ZReport"
					FileOutput.writeToFile(outputFile, "ZReport:", true, true);
					CommandManager.zReport();
				}
			} else {
				FileOutput.writeToFile(outputFile, "COMMAND: " + commands[0], true, true);
				FileOutput.writeToFile(outputFile, "ERROR: First command must be set initial time! Program is going to terminate!", true, true);
			}
		} else {
			FileOutput.writeToFile(outputFile, "ERROR: Something went wrong. Program is going to terminate!", true, true);
		}

	}
}