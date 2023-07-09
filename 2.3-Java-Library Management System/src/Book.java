import java.time.LocalDate;

public class Book {
	private static int bookID = 0;
	private boolean isBusy = false;
	private final int id;
	private Member member;
	private LocalDate date;

	private LocalDate deadline;
	private boolean isExtended = false;

	public Book() {
		bookID++;
		id = bookID;
	}

	static Book findBook(int id) throws Exception {
		for (Book book : CommandManager.handwritten) {
			if (book.getId() == id)
				return book;
		}

		for (Book book : CommandManager.printed) {
			if (book.getId() == id)
				return book;
		}

		throw new Exception(); // If there is no such book.
	}

	static void takeBook(String[] words) throws Exception {
		Book book = findBook(Integer.parseInt(words[1]));
		Member member = Member.findMember(Integer.parseInt(words[2]));

		if (book instanceof Handwritten && member instanceof Student) {
			FileOutput.print("Students can not read handwritten books!");
		} else if (book.isBusy()) {
			FileOutput.print("You can not read this book!");
		} else if ((member instanceof Student && (member.getBorrowedBooks() == 2)) || (member instanceof Academic && (member.getBorrowedBooks() == 4))) {
			FileOutput.print("You have exceeded the borrowing limit!");
		} else {
			LocalDate date = LocalDate.parse(words[3]);
			book.setDate(date);
			book.setBusy(true);
			book.setMember(member);
			if (member instanceof Student)
				book.setDeadline(date.plusDays(7));
			else
				book.setDeadline(date.plusDays(14));

			switch (words[0]) {
				case "borrowBook":
					member.setBorrowedBooks(+1);
					CommandManager.borrowed.add(book);
					FileOutput.print(String.format("The book [%s] was borrowed by member [%s] at %s", book.getId(), member.getId(), date));
					break;
				case "readInLibrary":
					CommandManager.inLibrary.add(book);
					FileOutput.print(String.format("The book [%s] was read in library by member [%s] at %s", book.getId(), member.getId(), date));
					break;
			}
		}
	}

	static void extendBook(String[] words) throws Exception {
		Book book = findBook(Integer.parseInt(words[1]));
		Member member = book.getMember();

		if (book.isExtended()) { // If the deadline for the book has already been extended.
			FileOutput.print("You cannot extend the deadline!");
		} else {
			if (member instanceof Student)
				book.setDeadline(book.getDeadline().plusDays(7));
			else
				book.setDeadline(book.getDeadline().plusDays(14));
			book.setExtended(true); // To check that the book has already been extended, later.
			FileOutput.print(String.format("The deadline of book [%s] was extended by member [%s] at %s", book.getId(), member.getId(), words[3]));
			FileOutput.print(String.format("New deadline of book [%s] is %s", book.getId(), book.getDeadline()));
		}

	}

	static void returnBook(String[] words) throws Exception {
		Book book = findBook(Integer.parseInt(words[1]));
		Member member = book.getMember();
		LocalDate current = LocalDate.parse(words[3]);
		if (book.getDeadline().isBefore(current))
			member.setFee(current.getDayOfYear() - book.getDeadline().getDayOfYear());
		// All of the following commands clear information about the book.
		CommandManager.inLibrary.remove(book);
		CommandManager.borrowed.remove(book);
		member.setBorrowedBooks(-1);
		book.setBusy(false);
		book.setMember(null);
		book.setDeadline(null);
		book.setDate(null);

		FileOutput.print(String.format("The book [%s] was returned by member [%s] at %s Fee: %d", book.getId(), member.getId(), current, member.getFee()));
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public int getId() {
		return id;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusy(boolean busy) {
		isBusy = busy;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public boolean isExtended() {
		return isExtended;
	}

	public void setExtended(boolean extended) {
		isExtended = extended;
	}
}

class Printed extends Book {
	public Printed() {
		super();
		FileOutput.print(String.format("Created new book: Printed [id: %s]", super.getId()));
	}

	@Override
	public String toString() {
		return String.format("Printed [id: %s]", super.getId());
	}
}

class Handwritten extends Book {
	public Handwritten() {
		super();
		FileOutput.print(String.format("Created new book: Handwritten [id: %s]", super.getId()));
	}

	@Override
	public String toString() {
		return String.format("Handwritten [id: %s]", super.getId());
	}
}
