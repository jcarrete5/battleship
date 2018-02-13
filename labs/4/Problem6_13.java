public class Problem6_13 {

	public static void main(String[] args) {
		System.out.println("i\t|\tm(i)");
		for (int i = 0; i < 20; i++) {
			System.out.printf("%-2d\t|\t%7.4f\n", i + 1, m(i + 1));
		}
	}

	private static double m(double i) {
		if (i <= 0) return 0;
		return i / (i + 1) + m(i - 1);
	}
}
