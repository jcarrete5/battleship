import java.util.Arrays;
import java.util.Scanner;

public class Problem7_9 {

	public static void main(String[] args) {
		double[] nums = new double[10];
		Scanner in = new Scanner(System.in);
		System.out.print("Enter ten numbers: ");
		for (int i = 0; i < 10; i++) {
			nums[i] = in.nextDouble();
		}
		in.close();

		System.out.println("The minimum number is: " + min(nums));
	}

	public static double min(double[] array) {
		return Arrays.stream(array).min().getAsDouble();
	}
}
