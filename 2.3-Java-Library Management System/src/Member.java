public class Member {
	private static int memberID = 0;
	private final int id;
	private int borrowedBooks = 0;
	private int fee = 0;

	public Member() {
		memberID++;
		id = memberID;
	}

	static Member findMember(int id) throws Exception {
		for (Member member : CommandManager.students) {
			if (member.getId() == id)
				return member;
		}

		for (Member member : CommandManager.academics) {
			if (member.getId() == id)
				return member;
		}

		throw new Exception(); // If there is no such member.
	}

	public int getId() {
		return id;
	}

	public int getBorrowedBooks() {
		return borrowedBooks;
	}

	public void setBorrowedBooks(int counter) {
		this.borrowedBooks += counter;
	}

	public int getFee() {
		return fee;
	}

	public void setFee(int fee) {
		this.fee = fee;
	}
}

class Student extends Member {
	public Student() {
		super();
		FileOutput.print(String.format("Created new member: Student [id: %s]", super.getId()));
	}

	@Override
	public String toString() {
		return String.format("Student [id: %s]", super.getId());
	}
}

class Academic extends Member {
	public Academic() {
		super();
		FileOutput.print(String.format("Created new member: Academic [id: %s]", super.getId()));
	}

	@Override
	public String toString() {
		return String.format("Academic [id: %s]", super.getId());
	}
}
