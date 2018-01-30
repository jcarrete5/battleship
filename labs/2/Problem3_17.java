import java.util.Scanner;

public class Problem3_17 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("scissor (0), rock (1), paper (2): ");
		int choice = in.nextInt();
		in.close();

		int comp = (int)(Math.random() * 3);
		System.out.print("The computer is " + toName(comp) + ". You are " + toName(choice) + ".");
		if (choice == comp) {
			System.out.println(" It is a draw.");
		} else if (choice == 0) {
			if (comp == 1) {
				System.out.println(" You lost.");
			} else {
				System.out.println(" You won.");
			}
		} else if (choice == 1) {
			if (comp == 0) {
				System.out.println(" You won.");
			} else {
				System.out.println(" You lost.");
			}
		} else if (choice == 2) {
			if (comp == 1) {
				System.out.println(" You won.");
			} else {
				System.out.println(" You lost.");
			}
		}
	}

	private static String toName(int choice) {
		if (choice == 0) return "scissor";
		if (choice == 1) return "rock";
		if (choice == 2) return "paper";
		throw new IllegalArgumentException("Invalid choice");
	}
}
