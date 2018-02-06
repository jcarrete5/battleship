public class Problem5_13 {

	public static void main(String[] args) {
		final int LIMIT = 12000;
		int n = 0;
		while (Math.pow(n + 1, 3) < LIMIT) { n++; }

		System.out.println(n + " is the largest n such that n^3 is less than " + LIMIT);
	}
}
