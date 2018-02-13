import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.DoubleStream;

public class Problem7_11 {

	public static void main(String[] args) {
		double[] nums = new double[10];
		Scanner in = new Scanner(System.in);
		System.out.print("Enter ten numbers: ");
		for (int i = 0; i < 10; i++) {
			nums[i] = in.nextDouble();
		}
		in.close();

		System.out.printf("The mean is %.2f\n", mean(nums));
		System.out.printf("The standard deviation is %.5f\n", deviation(nums));
	}

	/** Compute the deviation of double values */
	public static double deviation(double[] x) {
		double mean = Arrays.stream(x).average().getAsDouble();
		double sqSum = Arrays.stream(x).map(num -> Math.pow(num - mean, 2)).sum();
		return Math.sqrt(sqSum / (x.length - 1));
	}

	/** Compute the mean of an array of double values */
	public static double mean(double[] x) {
		return Arrays.stream(x).average().getAsDouble();
	}
}
