import java.util.Scanner;

public class Problem10_5 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a positive integer: ");
		int a = in.nextInt();
		in.close();

		if (a <= 0) {
			System.out.println("a must be > 0");
			return;
		}

		StackOfIntegers stack = new StackOfIntegers();
		for (int i = 2; i <= a;) {
			if (a % i == 0) {
				a /= i;
				stack.push(i);
			} else {
				i++;
			}
		}
		stack.printStack();
	}
}
