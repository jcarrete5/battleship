import java.util.Scanner;

public class Problem9_11 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a, b, c, d, e, and f: ");
		LinearEquation eq = new LinearEquation(in.nextDouble(), in.nextDouble(), in.nextDouble(),
				in.nextDouble(), in.nextDouble(), in.nextDouble());
		in.close();

		if (eq.isSolvable()) {
			System.out.println("x = " + eq.getX());
			System.out.println("y = " + eq.getY());
		} else {
			System.out.println("The equation has no solution");
		}
	}

	private static class LinearEquation {

		private double a, b, c, d, e, f;

		public LinearEquation(double a, double b, double c, double d, double e, double f) {
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
			this.e = e;
			this.f = f;
		}

		public boolean isSolvable() {
			return a * d - b * c != 0;
		}

		public double getX() {
			return (e * d - b * f) / (a * d - b * c);
		}

		public double getY() {
			return (a * f - e * c) / (a * d - b * c);
		}

		public double getA() {
			return a;
		}

		public double getB() {
			return b;
		}

		public double getC() {
			return c;
		}

		public double getD() {
			return d;
		}

		public double getE() {
			return e;
		}

		public double getF() {
			return f;
		}
	}
}
