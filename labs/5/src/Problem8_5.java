import java.util.Scanner;

public class Problem8_5 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a 3x3 matrix 1: ");
		double[][] a = new double[3][3];
		double[][] b = new double[3][3];
		for (int row = 0; row < a.length; row++) {
			for (int col = 0; col < a[row].length; col++) {
				a[row][col] = in.nextDouble();
			}
		}
		System.out.print("Enter a 3x3 matrix 2: ");
		for (int row = 0; row < b.length; row++) {
			for (int col = 0; col < b[row].length; col++) {
				b[row][col] = in.nextDouble();
			}
		}
		in.close();

		double[][] c = addMatrix(a, b);

		System.out.println("The matrices are added as follows");
		for (int row = 0; row < 3; row++) {
			String format;
			if (row == 1) {
				format = "%.1f %.1f %.1f +\t%.1f %.1f %.1f =\t%.1f %.1f %.1f\n";
			} else {
				format = "%.1f %.1f %.1f\t\t%.1f %.1f %.1f\t\t%.1f %.1f %.1f\n";
			}

			System.out.printf(format, a[row][0], a[row][1], a[row][2],
					b[row][0], b[row][1], b[row][2],
					c[row][0], c[row][1], c[row][2]);
		}
	}

	public static double[][] addMatrix(double[][] a, double[][] b) {
		double[][] c = new double[a.length][a[0].length];
		for (int row = 0; row < a.length; row++) {
			for (int col = 0; col < a[row].length; col++) {
				c[row][col] = a[row][col] + b[row][col];
			}
		}
		return c;
	}
}
