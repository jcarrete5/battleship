import java.util.Scanner;

public class Problem2_5 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the subtotal and a gratuity rate: ");
		double subtotal = in.nextDouble();
		double gratuityRate = in.nextDouble();
		in.close();

		double gratuity = subtotal * (gratuityRate / 100);
		double total = subtotal + gratuity;
		System.out.println("The gratuity is $" + gratuity + " and total is $" + total);
	}
}
