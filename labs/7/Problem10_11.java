public class Problem10_11 {

	public static void main(String[] args) {
		Circle2D c1 = new Circle2D(2, 2, 5.5);
		System.out.printf("c1's area is %f\n", c1.getArea());
		System.out.printf("c1's perimeter is %f\n", c1.getPerimeter());
		System.out.printf("c1 %s (3, 3)\n",
				c1.contains(3, 3) ? "contains" : "doesn't contain");
		System.out.printf("c1 %s Circle2D(4, 5, 10.5)\n",
				c1.contains(new Circle2D(4, 5, 10.5)) ? "contains" : "doesn't contain");
		System.out.printf("c1 %s Circle2D(3, 5, 2.3)\n",
				c1.overlaps(new Circle2D(3, 5, 2.3)) ? "overlaps" : "doesn't overlap");
	}
}
