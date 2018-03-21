public class Problem10_3 {

	public static void main(String[] args) {
		MyInteger a1 = new MyInteger(8);
		MyInteger a2 = new MyInteger(8);
		MyInteger a3 = new MyInteger(9);

		System.out.println("Value of a1 is " + a1.getValue());
		System.out.println("Value of a2 is " + a2.getValue());
		System.out.println("Value of a3 is " + a3.getValue());
		System.out.println(String.format("a1 is %s", a1.isEven() ? "even" : "not even"));
		System.out.println(String.format("a1 is %s", a1.isOdd() ? "odd" : "not odd"));
		System.out.println(String.format("a1 is %s", a1.isPrime() ? "prime" : "not prime"));
		System.out.println(String.format("a3 is %s", a3.isPrime() ? "prime" : "not prime"));
		System.out.println(String.format("a1 %s 8", a1.equals(8) ? "equals" : "doesn't equal"));
		System.out.println(String.format("a1 %s a2", a1.equals(a2) ? "equals" : "doesn't equal"));
		System.out.println(String.format("a1 %s a3", a1.equals(a3) ? "equals" : "doesn't equal"));
		System.out.println(String.format("17 is %s", MyInteger.isEven(17) ? "even" : "not even"));
		System.out.println(String.format("17 is %s", MyInteger.isOdd(17) ? "odd" : "not odd"));
		System.out.println(String.format("17 is %s", MyInteger.isPrime(17) ? "prime" : "not prime"));

		System.out.printf("\"876\" as int is %d\n", MyInteger.parseInt("876"));
		System.out.printf("'876' char array as int is %d\n", MyInteger.parseInt(new char[] {'8', '7', '6'}));
	}
}
