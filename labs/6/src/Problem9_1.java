public class Problem9_1 {

	public static void main(String[] args) {
		Rectangle r1 = new Rectangle(4, 40);
		Rectangle r2 = new Rectangle(3.5, 35.9);
		System.out.println("r1 {" +
				"width: " + r1.width + ", " +
				"height: " + r1.height + ", " +
				"area: " + r1.getArea() + ", " +
				"perimeter: " + r1.getPerimeter() + "}");
		System.out.println("r2 {" +
				"width: " + r2.width + ", " +
				"height: " + r2.height + ", " +
				"area: " + r2.getArea() + ", " +
				"perimeter: " + r2.getPerimeter() + "}");
	}


	private static class Rectangle {

		private double width, height;

		public Rectangle() {
			width = height = 1;
		}

		public Rectangle(double w, double h) {
			width = w;
			height = h;
		}

		public double getArea() {
			return width * height;
		}

		public double getPerimeter() {
			return 2 * width + 2 * height;
		}
	}
}
