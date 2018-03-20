public class Problem10_9 {

	public static void main(String[] args) {
		Course eng100 = new Course("ENG 100");
		eng100.addStudent("James Gill");
		eng100.addStudent("Robust Rhonda");
		eng100.addStudent("Squilliam Squill");
		eng100.dropStudent("James Gill");
		System.out.println(eng100.getCourseName() + " with " + eng100.getNumberOfStudents() + " students");
		for (String student : eng100.getStudents()) {
			if (student != null) {
				System.out.println(student);
			}
		}
	}
}
