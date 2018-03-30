import java.util.ArrayList;
import java.util.Iterator;

public class Course {

	private String courseName;
	private ArrayList<String> students = new ArrayList<>(100);
	private int numberOfStudents;

	public Course(String courseName) {
		this.courseName = courseName;
	}

	public void addStudent(String student) {
		students.add(student);
		numberOfStudents++;
	}

	public String[] getStudents() {
		return (String[])students.toArray();
	}

	public int getNumberOfStudents() {
		return numberOfStudents;
	}

	public String getCourseName() {
		return courseName;
	}

	public void dropStudent(String student) {
		Iterator<String> iter = students.iterator();
		while (iter.hasNext()) {
			if (iter.next().equals(student)) {
				iter.remove();
				numberOfStudents--;
				break;
			}
		}
	}

	public void clear() {
		students.clear();
		numberOfStudents = 0;
	}
}
