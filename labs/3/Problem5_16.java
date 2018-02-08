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
				builder.append(factor).append(' ');
			} else {
				factor = nextPrime(factor);
			}
		}
		String output = builder.toString().trim().replace(" ", ", ");
		System.out.println(output);
	}

	private static int nextPrime(int n) {
		if (n < 2) throw new IllegalArgumentException("n must be greater than or equal to 2");
		n = n % 2 == 0 ? n + 1 : n + 2;
		while (!isPrime(n)) {
			n += 2;
		}
		return n;
	}

	private static boolean isPrime(int n) {
		if (n < 2) throw new IllegalArgumentException("n must be greater than or equal to 2");
		if (n == 2) return true;
		if (n % 2 == 0) return false;
		for (int i = 3; i <= (int)Math.ceil(Math.sqrt(n)); i += 2) {
			if (n % i == 0) return false;
		}
		return true;
	}
}
