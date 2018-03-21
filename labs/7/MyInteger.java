public class MyInteger {

	private int value;

	public static boolean isEven(int a) {
		return a % 2 == 0;
	}

	public static boolean isOdd(int a) {
		return !isEven(a);
	}

	public static boolean isPrime(int a) {
		if (a <= 2) return true;

		for (int i = 2; i <= Math.floor(Math.sqrt(a)); i++) {
			if (a % i == 0) return false;
		}

		return true;
	}

	public static boolean isEven(MyInteger a) {
		return a.isEven();
	}

	public static boolean isOdd(MyInteger a) {
		return a.isOdd();
	}

	public static boolean isPrime(MyInteger a) {
		return a.isPrime();
	}

	public static int parseInt(char[] arr) {
		return Integer.parseInt(new String(arr));
	}

	public static int parseInt(String s) {
		return Integer.parseInt(s);
	}

	public MyInteger(int value) {
		this.value = value;
	}

	public boolean isEven() {
		return isEven(value);
	}

	public boolean isOdd() {
		return isOdd(value);
	}

	public boolean isPrime() {
		return isPrime(value);
	}

	public int getValue() {
		return value;
	}

	public boolean equals(int a) {
		return value == a;
	}

	public boolean equals(MyInteger a) {
		return value == a.getValue();
	}
}
