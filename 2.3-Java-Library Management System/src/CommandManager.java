import java.util.ArrayList;

public class CommandManager {
	public static final ArrayList<Student> students = new ArrayList<>();
	public static final ArrayList<Academic> academics = new ArrayList<>();
	public static final ArrayList<Printed> printed = new ArrayList<>();
	public static final ArrayList<Handwritten> handwritten = new ArrayList<>();
	public static final ArrayList<Book> borrowed = new ArrayList<>();
	public static final ArrayList<Book> inLibrary = new ArrayList<>();
	public static String outputFile;

	public static void run(String[] commands, String outputFile) {
		CommandManager.outputFile = outputFile;
		for (String command : commands) {
			try {
				String[] words = command.split("\t");

				switch (words[0]) {
					case "addBook":
						addBook(words);
						break;
					case "addMember":
						addMember(words);
						break;
					case "borrowBook":
					case "readInLibrary":
						Book.takeBook(words); // Checks later where to read the book so that the code is cleaner.
						break;
					case "extendBook":
						Book.extendBook(words);
						break;
					case "returnBook":
						Book.returnBook(words);
						break;
					case "getTheHistory":
						getTheHistory();
						break;
				}
			} catch (Exception e) {
				FileOutput.print("Something went wrong! " + e);
			}
		}
	}

	private static void addBook(String[] words) {
		switch (words[1]) {
			case "H":
				handwritten.add(new Handwritten());
				break;
			case "P":
				printed.add(new Printed());
				break;
		}
	}

	private static void addMember(String[] words) {
		switch (words[1]) {
			case "A":
				academics.add(new Academic());
				break;
			case "S":
				students.add(new Student());
				break;
		}
	}

	private static void getTheHistory() {
		FileOutput.print("History of library:\n");

		FileOutput.print("Number of students: " + students.size());
		for (Student student : students)
			FileOutput.print(student.toString());

		FileOutput.print("\nNumber of academics: " + students.size());
		for (Academic academic : academics)
			FileOutput.print(academic.toString());

		FileOutput.print("\nNumber of printed books: " + printed.size());
		for (Printed printed : printed)
			FileOutput.print(printed.toString());

		FileOutput.print("\nNumber of handwritten books: " + handwritten.size());
		for (Handwritten handwritten : handwritten)
			FileOutput.print(handwritten.toString());

		FileOutput.print("\nNumber of borrowed books: " + borrowed.size());
		for (Book book : borrowed)
			FileOutput.print(String.format("The book [%s] was borrowed by member [%s] at %s", book.getId(), book.getMember().getId(), book.getDate()));

		FileOutput.print("\nNumber of books read in library: " + inLibrary.size());
		for (Book book : inLibrary)
			FileOutput.print(String.format("The book [%s] was read in library by member [%s] at %s", book.getId(), book.getMember().getId(), book.getDate()));
	}
}