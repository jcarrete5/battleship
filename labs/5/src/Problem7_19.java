import java.util.Scanner;

public class Problem7_19 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter list: ");
		final int SIZE = in.nextInt();
		int[] nums = new int[SIZE];
		for (int i = 0; i < nums.length; i++) {
			nums[i] = in.nextInt();
		}
		in.close();

		System.out.println(isSorted(nums) ? "The list is already sorted." : "The list is not sorted.");
	}

	public static boolean isSorted(int[] list) {
		int last = list[0];
		for (int n : list) {
			if (n < last) {
				return false;
			}
			last = n;
		}
		return true;
	}
}
