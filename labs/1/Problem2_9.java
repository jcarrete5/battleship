import java.util.Scanner;

public class Problem2_9 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter v0, v1, and t: ");
		double v0 = in.nextDouble();
		double v1 = in.nextDouble();
		double t = in.nextDouble();
		in.close();

		double acc = (v1 - v0) / t;

		System.out.println("The average acceleration is " + acc);
	}
}
