import java.util.Scanner;

public class Problem6_5 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter 3 numbers to be sorted: ");
		displaySortedNumbers(in.nextDouble(), in.nextDouble(), in.nextDouble());
		in.close();
	}

	public static void displaySortedNumbers(double num1, double num2, double num3) {
		double max, mid;
		double min = Math.min(num1, Math.min(num2, num3));
		if (num1 == min) {
			max = Math.max(num2, num3);
			mid = Math.min(num2, num3);
		} else if (num2 == min) {
			max = Math.max(num1, num3);
			mid = Math.min(num1, num3);
		} else {
			max = Math.max(num1, num2);
			mid = Math.min(num1, num2);
		}

		System.out.println(min + ", " + mid + ", " + max);
	}
}
