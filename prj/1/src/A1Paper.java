import java.util.Scanner;

public class A1Paper {

	public static void main(String[] args) {
		// Pre-compute lengths (in meters) of the long sides of each type of paper
		final double root2 = Math.sqrt(2.0);
		final double[] longLengths = new double[29];
		longLengths[0] = Math.pow(2, -0.75);
		for (int i = 1; i < longLengths.length; i++) {
			longLengths[i] = (longLengths[i - 1] / 2) * root2;
		}

		Scanner in = new Scanner(System.in);
		int n = in.nextInt();
		int[] sheetCount = new int[n - 1];  // Index 0 is number of A2 paper ...
		for (int i = 0; i < sheetCount.length; i++) {
			sheetCount[i] = in.nextInt();
		}
		in.close();

		double tapeLen = 0;
		for (int i = sheetCount.length - 1; i >= 0; i--) {
			int newSheets = sheetCount[i] / 2;
			tapeLen += longLengths[i] * newSheets;
			sheetCount[i] -= newSheets * 2;
			if (i > 0) {
				sheetCount[i - 1] += newSheets;
			}
		}

		if (sheetCount[0] == 2) {
			System.out.println(tapeLen);
		} else {
			System.out.println("impossible");
		}
	}
}
