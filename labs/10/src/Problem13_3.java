import java.util.ArrayList;
import java.util.Arrays;

public class Problem13_3 {

	public static void main(String[] args) {
		ArrayList<Number> list = new ArrayList<>(Arrays.asList(10, 9, 8, 7, 6, 5, 4, 3, 2, 1));
		sort(list);
		System.out.println(list);
	}

	public static void sort(ArrayList<Number> list) {
		for (int i = 0; i < list.size() - 1; i++) {
			for (int j = 0; j < list.size() - 1 - i; j++) {
				if (list.get(j).doubleValue() > list.get(j + 1).doubleValue()) {
					list.set(j, list.set(j + 1, list.get(j)));
				}
			}
		}
	}
}
