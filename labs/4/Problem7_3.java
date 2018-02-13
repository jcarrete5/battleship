import java.util.Scanner;

public class Problem7_3 {

	public static void main(String[] args) {
		int[] nums = new int[100];

		Scanner in = new Scanner(System.in);
		System.out.print("Enter integers between 1 and 100 (stop with 0): ");
		int n = in.nextInt();
		while (n != 0) {
			nums[n - 1]++;
			n = in.nextInt();
		}
		in.close();

		for (int i = 0; i < nums.length; i++) {
			int times = nums[i];
			if (times == 0) continue;
			System.out.println((i + 1) + " occurs " + times + " time" + (times > 1 ? "s" : ""));
		}
	}
}
