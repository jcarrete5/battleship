public class Problem6_9 {

	public static void main(String[] args) {
		System.out.println("Feet\tMeters\t|\tMeters\tFeet");
		for (int i = 0; i < 10; i++) {
			double meters = footToMeter(i + 1);
			double feet = meterToFoot(i * 5 + 20);
			System.out.printf("%4.1f\t%7.3f\t|\t%4.1f\t%7.3f\n", (double)(i + 1), meters,
					(double)(i * 5 + 20), feet);
		}
	}

	public static double footToMeter(double foot) {
		return 0.305 * foot;
	}

	public static double meterToFoot(double meter) {
		return 3.279 * meter;
	}
}
