import java.util.Scanner;

public class Problem6_3 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter an integer: ");
		int n = in.nextInt();
		int absN = Math.abs(n);
		in.close();

		if (isPalindrome(absN)) {
			System.out.println(n + " is a palindrome");
		} else {
			System.out.println(n + " is not a palindrome");
		}
	}

	public static int reverse(int number) {
		int digit = String.valueOf(number).length() - 1;
		int reversed = 0;

		while (number > 0) {
			reversed += (int)Math.pow(10, digit) * (number % 10);
			number /= 10;
			digit--;
		}

		return reversed;
	}

	public static boolean isPalindrome(int number) {
		return number == reverse(number);
	}
}
