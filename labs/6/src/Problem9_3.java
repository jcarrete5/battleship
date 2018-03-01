import java.util.Date;

public class Problem9_3 {

	public static void main(String[] args) {
		Date date = new Date(0);
		for (long msSinceEpoch = 10000; msSinceEpoch <= 100000000000L; msSinceEpoch *= 10) {
			date.setTime(msSinceEpoch);
			System.out.println(msSinceEpoch + "ms after epoch: " + date.toString());
		}
	}
}
