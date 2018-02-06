import java.util.Scanner;

public class Problem5_16 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter an integer: ");
		int n = Math.abs(in.nextInt());
		in.close();

		StringBuilder builder = new StringBuilder();
		int factor = 2;
		while (n > 1) {
			if (n % factor == 0) {
				n /= factor;
				builder.append(factor + " ");
			} else {
				factor++; // Slightly inefficient since we really want factor to be the next prime
			}
		}
		String output = builder.toString().trim().replace(" ", ", ");
		System.out.println(output);
	}
}
