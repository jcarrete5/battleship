import java.util.Scanner;

public class Problem2_7 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the number of minutes: ");
		int minutes = in.nextInt();
		in.close();

		int years = minutes / 60 / 24 / 365;
		int days = minutes / 60 / 24 - years * 365;
		System.out.println(minutes + " minutes is approximately " + years + " years and " + days + " days");
	}
}
