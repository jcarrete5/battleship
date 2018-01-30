import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.Scanner;

public class Problem3_11 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the month: ");
		Month m = Month.of(in.nextInt());
		System.out.print("Enter the year: ");
		Year y = Year.of(in.nextInt());
		in.close();

		System.out.println(m.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
			+ " " + y.toString() + " had " + m.length(y.isLeap()) + " days");
	}
}
