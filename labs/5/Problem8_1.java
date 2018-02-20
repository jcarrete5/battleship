import java.util.Scanner;

public class Problem8_1 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.println("Enter a 3-by-4 matrix row by row:");
		double[][] m = new double[3][4];
		for (int row = 0; row < m.length; row++) {
			for (int col = 0; col < m[row].length; col++) {
				m[row][col] = in.nextDouble();
			}
		}
		in.close();

		for (int col = 0; col < m[0].length; col++) {
			System.out.println("Sum of the elements at column " + col + " is " + sumColumn(m, col));
		}
	}

	public static double sumColumn(double[][] m, int columnIndex) {
		double sum = 0.0;
		for (int row = 0; row < m.length; row++) {
			sum += m[row][columnIndex];
		}
		return sum;
	}
}
