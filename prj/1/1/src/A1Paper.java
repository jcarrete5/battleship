import java.util.Scanner;

public class A1Paper {

	public static void main(String[] args) {
		final double NEEDED_A0PROP = 0.5;
		double A0prop = 0.0;  // Proportion of A0 paper we currently have
		double tapeLen = 0.0;  // Length of tape in meters
		boolean possible = false;

		Scanner in = new Scanner(System.in);
		final int N = in.nextInt();
		for (int paperSize = 2; paperSize <= N; paperSize++) {
			final int numSheets = in.nextInt();
			final double sideLength = longSideLength(paperSize);
			final double prop = Math.pow(0.5, paperSize);  // Proportion of A0

			final int neededSheets = (int)((NEEDED_A0PROP - A0prop) / prop);
			tapeLen += neededSheets / 2 * sideLength;
			if (neededSheets - numSheets <= 0) {
				possible = true;
				break;
			}

			A0prop += prop * numSheets;
		}
		in.close();

		System.out.println(possible ? tapeLen : "impossible");
	}

	/**
	 * Computes the length of the longest side of a certain size
	 * of A-series paper, A<sub>n</sub>.
	 * @param n The size of the A-series paper.
	 * @return The length of the longest side of A<sub>n</sub>
	 */
	private static double longSideLength(final int n) {
		final double A0 = 1.189207115002721;
		final double ROOT2ON2 = 0.7071067811865476;
		return A0 * Math.pow(ROOT2ON2, n);
	}
}
