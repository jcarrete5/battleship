public class Problem5_5 {

	public static void main(String[] args) {
		final double kgPerPound = 0.453592;
		final double poundsPerKg = 1 / kgPerPound;

		System.out.println("Kilograms\tPounds\t|\tPounds\tKilograms");
		for (int i = 0; i <= 99; i++) {
			int kg = i * 2 + 1;
			int lbs = i * 5 + 20;
			System.out.printf("%-9d\t%6.1f\t|\t%-6d\t%9.2f\n", kg, kg * poundsPerKg, lbs,
					lbs * kgPerPound);
		}
	}
}
