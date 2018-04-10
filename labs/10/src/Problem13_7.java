public class Problem13_7 {

	public static void main(String[] args) {
		GeometricObject[] objs = {
			new Square(4), new Circle(), new Octogon(3), new Octogon(), new Square()
		};

		for (GeometricObject obj : objs) {
			System.out.println(obj.getArea());
			if (obj instanceof Colorable) {
				((Colorable)obj).howToColor();
			}
		}
	}
}
