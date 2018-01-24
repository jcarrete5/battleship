import java.util.Scanner;

public class Problem2_1 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a degree in Celsius: ");
		double celsius = in.nextDouble();
		in.close();

		double fahrenheit = (9.0 / 5.0) * celsius + 32;
		System.out.println(celsius + " Celsius is " + fahrenheit + " Fahrenheit");
	}
}
