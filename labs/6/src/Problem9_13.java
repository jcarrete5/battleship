import java.util.Scanner;

public class Problem9_13 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the number of rows and columns in the array: ");
		double[][] a = new double[in.nextInt()][in.nextInt()];
		System.out.println("Enter the array: ");
		for (int row = 0; row < a.length; row++) {
			for (int col = 0; col < a[row].length; col++) {
				a[row][col] = in.nextDouble();
			}
		}
		in.close();

		System.out.println(Location.locateLargest(a));
	}

	private static class Location {

		public int row, column;
		public double maxValue;

		public static Location locateLargest(double[][] a) {
			Location loc = new Location();
			for (int row = 0; row < a.length; row++) {
				for (int col = 0; col < a[row].length; col++) {
					if (a[row][col] > loc.maxValue) {
						loc.maxValue = a[row][col];
						loc.row = row;
						loc.column = col;
					}
				}
			}
			return loc;
		}

		@Override
		public String toString() {
			return String.format("The location of the largest element is %f at (%d, %d)", maxValue, row, column);
		}
	}
}
