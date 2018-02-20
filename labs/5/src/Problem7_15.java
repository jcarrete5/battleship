import java.util.*;
import java.util.stream.Collectors;

public class Problem7_15 {

	public static void main(String[] args) {
		int[] nums = new int[10];
		Scanner in = new Scanner(System.in);
		System.out.print("Enter ten numbers: ");
		for (int i = 0; i < 10; i++) {
			nums[i] = in.nextInt();
		}
		in.close();

		String result = Arrays.toString(eliminateDuplicates(nums)).replaceAll("(?:\\[|\\]|,)", "");
		System.out.println("The distinct numbers are: " + result);
	}

	public static int[] eliminateDuplicates(int[] list) {
		HashSet<Integer> set = new HashSet<>(Arrays.stream(list).boxed().collect(Collectors.toList()));
		int[] newList = new int[set.size()];
		int i = 0;
		for (int n : set.toArray(new Integer[0])) {
			newList[i] = n;
			i++;
		}
		return newList;
	}
}
