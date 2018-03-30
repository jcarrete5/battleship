import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Problem11_11 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter 5 integers: ");
		ArrayList<Integer> list = new ArrayList<>(5);
		for (int i = 0; i < 5; i++) {
			list.add(in.nextInt());
		}
		in.close();

		sort(list);
		System.out.println("Sorted: " + list);
	}

	public static void sort(ArrayList<Integer> list) {
		Collections.sort(list);
	}
}
