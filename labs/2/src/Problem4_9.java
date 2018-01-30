import java.util.Scanner;

public class Problem4_9 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a character: ");
		char ch = in.nextLine().charAt(0);
		in.close();

		System.out.println("The unicode for the character " + ch + " is " + (int)ch);
	}
}
