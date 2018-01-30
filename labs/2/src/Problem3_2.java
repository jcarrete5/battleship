import java.util.Scanner;

public class Problem3_2 {

	public static void main(String[] args) {
		int n1 = (int)(System.currentTimeMillis() % 10);
		int n2 = (int)(System.currentTimeMillis() / 7 % 10);
		int n3 = (int)(System.currentTimeMillis() / 3 % 10);

		Scanner in = new Scanner(System.in);
		System.out.print("What is " + n1 + " + " + n2 + " + " + n3 + "? ");
		int answer = in.nextInt();
		in.close();

		System.out.println(n1 + " + " + n2 + " + " + n3 + " = " + answer + " is " + (n1 + n2 + n3 == answer));
	}
}
