import java.util.Scanner;

public class Problem11_1 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter three sides of a triangle, the color of the triangle and if it is filled: ");
		Triangle t = new Triangle(in.nextDouble(), in.nextDouble(), in.nextDouble());
		t.setColor(in.next());
		t.setFilled(in.nextBoolean());
		in.close();

		System.out.println("The area of the triangle is: " + t.getArea());
		System.out.println("The perimeter of the triangle is: " + t.getPerimeter());
		System.out.println("The color of the triangle is: " + t.getColor());
		System.out.println("The triangle " + (t.isFilled() ? "is" : "isn't") + " filled");
	}
}
