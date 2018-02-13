public class MyTriangle {

	/**
	 * Return true if the sum of any two sides is greater than the third side.
	 */
	public static boolean isValid(double side1, double side2, double side3) {
		return side1 + side2 > side3 && side1 + side3 > side2 && side2 + side3 > side1;
	}

	/**
	 * Return the area of the triangle.
	 */
	public static double area(double side1, double side2, double side3) {
		double p = (side1 + side2 + side3) / 2;  // half-perimeter
		return Math.sqrt(p * (p - side1) * (p - side2) * (p - side3));
	}
}
