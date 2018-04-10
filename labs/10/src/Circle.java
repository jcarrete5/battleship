public class Circle extends GeometricObject {

	private double radius;

	public Circle() {
		this(1);
	}

	public Circle(double radius) {
		super();
		this.radius = radius;
	}

	@Override
	public double getArea() {
		return Math.PI * radius * radius;
	}

	@Override
	public double getPerimeter() {
		return 2 * Math.PI * radius;
	}
}
