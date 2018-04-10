public class Problem13_11 {

	public static void main(String[] args) {
		Octagon o1 = new Octagon(5);
		System.out.println("o1 area = " + o1.getArea());
		System.out.println("o1 perimeter = " + o1.getPerimeter());
		Octagon clone = (Octagon)o1.clone();
		System.out.println("Comparing o1 to clone result: " + o1.compareTo(clone));
	}
}
