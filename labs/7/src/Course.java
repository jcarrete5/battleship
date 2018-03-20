public class Course {

	private String courseName;
	private String[] students = new String[100];
	private int numberOfStudents;

	public Course(String courseName) {
		this.courseName = courseName;
	}

	public void addStudent(String student) {
		if (numberOfStudents == students.length) {
			String[] newStudents = new String[numberOfStudents * 2];
			System.arraycopy(students, 0, newStudents, 0, numberOfStudents);
			students = newStudents;
		}
		students[numberOfStudents] = student;
		numberOfStudents++;
	}

	public String[] getStudents() {
		return students;
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public String getCourseName() {
		return courseName;
	}

	public void dropStudent(String student) {
		boolean found = false;
		for (int i = 0; i < numberOfStudents; i++) {
			if (students[i].equals(student)) {
				found = true;
			}

			if (found && i + 1 < students.length) {
				students[i] = students[i + 1];
			}
		}

		if (found) {
			numberOfStudents--;
		}
	}

	public void clear() {
		students = new String[100];
	}
}