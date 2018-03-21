public class Circle2D {

	private double x, y;
	private double radius;

	public Circle2D() {
		x = y = 0;
		radius = 1;
	}

	public Circle2D(double x, double y, double radius) {
		this.x = x;
		this.y = y;
		this.radius = radius;
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getRadius() {
		return radius;
	}

	public double getArea() {
		return Math.PI * radius * radius;
	}

	public double getPerimeter() {
		return 2 * Math.PI * radius;
	}

	public boolean contains(double x, double y) {
		// translate point
		double tx = x - this.x;
		double ty = y - this.y;

		return Math.sqrt(tx*tx + ty*ty) < radius;
	}

	public boolean contains(Circle2D c) {
		// translate circle
		double tx = c.x - this.x;
		double ty = c.y - this.y;

		return Math.sqrt(tx*tx + ty*ty) + c.radius < radius;
	}

	public boolean overlaps(Circle2D c) {
		// translate circle
		double tx = c.x - this.x;
		double ty = c.y - this.y;

		return !this.contains(c) && Math.sqrt(tx*tx + ty*ty) < c.radius + radius;
	}
}
