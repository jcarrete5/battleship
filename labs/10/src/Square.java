public class Square extends GeometricObject implements Colorable {

	private double s;

	public Square() {
		this(1);
	}

	public Square(double sideLength) {
		super();
		s = sideLength;
	}

	@Override
	public double getArea() {
		return s * s;
	}

	@Override
	public double getPerimeter() {
		return 4 * s;
	}

	@Override
	public void howToColor() {
		System.out.println("Color all four sides");
	}
}
