import java.util.GregorianCalendar;

public class Problem9_5 {

	public static void main(String[] args) {
		GregorianCalendar cal = new GregorianCalendar();

		System.out.printf("Current year, month, and day: %s, %s, %s\n",
				cal.get(GregorianCalendar.YEAR),
				cal.get(GregorianCalendar.MONTH),
				cal.get(GregorianCalendar.DAY_OF_MONTH));

		cal.setTimeInMillis(1234567898765L);
		System.out.printf("Year, month, and day 1234567898765ms passed epoch: %s, %s, %s\n",
				cal.get(GregorianCalendar.YEAR),
				cal.get(GregorianCalendar.MONTH),
				cal.get(GregorianCalendar.DAY_OF_MONTH));
	}
}
