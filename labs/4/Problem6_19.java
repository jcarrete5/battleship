import java.util.Scanner;

public class Problem6_19 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter 3 sides of a triangle: ");
		double s1 = in.nextDouble();
		double s2 = in.nextDouble();
		double s3 = in.nextDouble();
		in.close();

		if (MyTriangle.isValid(s1, s2, s3)) {
			System.out.println("The area of the triangle is: " + MyTriangle.area(s1, s2, s3));
		} else {
			System.out.println("That is not a valid triangle!");
		}
	}
}
