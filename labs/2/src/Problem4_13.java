import java.util.Scanner;

public class Problem4_13 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a letter: ");
		char ch = in.nextLine().charAt(0);
		in.close();

		if ("aeiouAEIUO".contains(String.valueOf(ch))) {
			System.out.println(ch + " is a vowel");
		} else if (Character.isLetter(ch)) {
			System.out.println(ch + " is a consonant");
		} else {
			System.out.println(ch + " is an invalid input");
		}
	}
}
