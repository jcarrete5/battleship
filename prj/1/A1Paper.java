import java.util.Scanner;

public class A1Paper {

	public static void main(String[] args) {
		int neededSheets = 2;
		double tapeLen = 0.0;  // Length of tape in meters
		boolean possible = false;

		Scanner in = new Scanner(System.in);
		final int N = in.nextInt();
		for (int paperSize = 2; paperSize <= N; paperSize++) {
			final int numSheets = in.nextInt();
			final double sideLength = longSideLength(paperSize);

			tapeLen += neededSheets / 2 * sideLength;

			if (numSheets >= neededSheets) {
				possible = true;
				break;
			}

			neededSheets = (neededSheets - numSheets) * 2;
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
