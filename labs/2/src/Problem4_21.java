import java.util.Scanner;

public class Problem4_21 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a SSN: ");
		String ssn = in.nextLine();
		in.close();

		if (ssn.matches("^\\d{3}-\\d{2}-\\d{4}$")) {
			System.out.println(ssn + " is a valid social security number");
		} else {
			System.out.println(ssn + " is an invalid social security number");
		}
	}
}
