import java.util.Scanner;

public class Problem5_1 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		int numPositive = 0;
		int numNegative = 0;
		int total = 0;
		int numbersEntered = 0;

		System.out.print("Enter some integers. Input ends if it 0: ");
		int n = in.nextInt();
		if (n == 0) {
			System.out.println("No numbers entered except 0");
			return;
		}

		while (n != 0) {
			if (n > 0) {
				numPositive++;
			} else {
				numNegative++;
			}
			total += n;
			numbersEntered++;
			n = in.nextInt();
		}

		in.close();

		System.out.println("The number of positives is " + numPositive);
		System.out.println("The number of negatives is " + numNegative);
		System.out.println("The total is " + total);
		System.out.println("The average is " + ((double)total / numbersEntered));
	}
}
