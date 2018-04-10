// Regular Octagon
public class Octagon extends GeometricObject implements Comparable<Octagon>, Cloneable {

	private static final double AREA_COEFF = 2 + 4 / Math.sqrt(2);

	private double side;

	public Octagon() {
		this(1);
	}

	public Octagon(double side) {
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

	@Override
	public int compareTo(Octagon o) {
		double ds = this.side - o.side;
		if (ds < 0) {
			return -1;
		} else if (ds > 0) {
			return 1;
		} else {
			return 0;
		}
	}

	@Override
	protected Object clone() {
		return new Octagon(this.side);
	}
}
