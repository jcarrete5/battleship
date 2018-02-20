import java.util.Scanner;

public class Problem8_11 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter a number between 0 and 511: ");
		int n = in.nextInt();
		in.close();

		for (int mask = 0x100; mask > 0; mask >>= 1) {
			char ch = (n & mask) == 0 ? 'H' : 'T';
			System.out.print(ch + ((mask & 0x49) == 0 ? " " : "\n"));
		}
	}
}
