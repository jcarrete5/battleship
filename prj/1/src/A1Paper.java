import java.util.Scanner;

public class A1Paper {

	public static void main(String[] args) {
		// Pre-compute lengths (in meters) of the long sides of each type of paper
		final double ROOT2 = Math.sqrt(2.0);
		final double[] longSideLengths = new double[30];
		longSideLengths[0] = Math.pow(2, -0.75);
		for (int i = 1; i < longSideLengths.length; i++) {
			longSideLengths[i] = (longSideLengths[i - 1] / 2) * ROOT2;
		}

		double tapeLen = 0.0;
		double A2pct = 0.0;
		boolean possible = false;

		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		for (int i = 0; i < n - 1; i++) {
			final int numSheets = in.nextInt();
			final double sideLength = longSideLengths[i];
			final double pct = Math.pow(2, -i - 1);

			int neededSheets = (int)((1.0 - A2pct) / pct);
			if (neededSheets - numSheets <= 0) {
				tapeLen += neededSheets / 2 * sideLength;
				possible = true;
				break;
			}

			tapeLen += neededSheets / 2 * sideLength;
			A2pct += pct * numSheets;
		}
		in.close();

		if (possible) {
			System.out.println(tapeLen);
		} else {
			System.out.println("impossible");
		}
	}
}
