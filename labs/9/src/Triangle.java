public class Triangle extends GeometricObject {

	private double s1, s2, s3;

	public Triangle() throws IllegalTriangleException {
		this(1, 1, 1);
	}

	public Triangle(double s1, double s2, double s3) throws IllegalTriangleException {
		super();
		if (s1 + s2 < s3 || s2 + s3 < s1 || s1 + s3 < s2) {
			throw new IllegalTriangleException();
		}
		this.s1 = s1;
		this.s2 = s2;
		this.s3 = s3;
	}

	public double getS1() {
		return s1;
	}

	public void setS1(double s1) {
		this.s1 = s1;
	}

	public double getS2() {
		return s2;
	}

	public void setS2(double s2) {
		this.s2 = s2;
	}

	public double getS3() {
		return s3;
	}

	public void setS3(double s3) {
		this.s3 = s3;
	}

	public double getArea() {
		double s = getPerimeter() / 2;
		return Math.sqrt(s * (s - s1) * (s - s2) * (s - s3));
	}

	public double getPerimeter() {
		return s1 + s2 + s3;
	}

	@Override
	public String toString() {
		return String.format("Triangle: {s1: %f, s2: %f, s3: %f}", s1, s2, s3);
	}
}
