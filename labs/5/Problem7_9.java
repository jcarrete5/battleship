import java.util.Arrays;
import java.util.Scanner;

public class Problem7_9 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		double[] numbers = new double[10];
		System.out.print("Enter 10 numbers: ");
		for (int i = 0; i < 10; i++) {
			numbers[i] = in.nextDouble();
		}
		in.close();

		System.out.println("The minimum number is: " + min(numbers));
	}

	public static double min(double[] array) {
		return Arrays.stream(array).min().getAsDouble();
	}
}
