// Regular Octogon
public class Octogon extends GeometricObject {

	private static final double AREA_COEFF = 2 + 4 / Math.sqrt(2);

	private double side;

	public Octogon() {
		this(1);
	}

	public Octogon(double side) {
		super();
		this.side = side;
	}

	@Override
	public double getArea() {
		return AREA_COEFF * side * side;
	}

	@Override
	public double getPerimeter() {
		return 8 * side;
	}
}
