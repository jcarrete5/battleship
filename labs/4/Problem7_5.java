import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Problem7_5 {

	public static void main(String[] args) {
		Set<Integer> distinct = new HashSet<>();

		Scanner in = new Scanner(System.in);
		System.out.print("Enter ten integers: ");
		for (int i = 0; i < 10; i++) {
			distinct.add(in.nextInt());
		}
		in.close();

		System.out.println("The number of distinct integers is: " + distinct.size());
		for (int n : distinct) {
			System.out.print(n + " ");
		}
		System.out.println();
	}
}
