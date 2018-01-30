import java.time.DayOfWeek;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class Problem3_5 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter today's day: ");
		DayOfWeek today = DayOfWeek.of(in.nextInt() + 1).minus(1); // Convert to ISO-8601
		System.out.print("Enter the number of days elapsed since today: ");
		int future = in.nextInt();
		in.close();

		System.out.println("Today is "
				+ today.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
				+ " and the future day is "
				+ today.plus(future).getDisplayName(TextStyle.FULL, Locale.ENGLISH));
	}
}
