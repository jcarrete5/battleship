import java.util.Scanner;

public class Problem12_3 {

	public static void main(String[] args) {
		// Populate array with random integers
		int[] arr = new int[100];
		for (int i = 0; i < 100; i++) {
			arr[i] = (int)(Math.random() * Integer.MAX_VALUE);
		}

		Scanner in = new Scanner(System.in);
		System.out.print("Enter an index: ");
		int index = in.nextInt();
		in.close();

		try {
			System.out.println(String.format("arr[%d] = %d", index, arr[index]));
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("Out of bounds");
		}
	}
}
