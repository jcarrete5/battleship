public class Problem11_3 {

	public static void main(String[] args) {
		Account acc1 = new Account(1232, 1000);
		SavingsAccount acc2 = new SavingsAccount(4325, 500);
		CheckingAccount acc3 = new CheckingAccount(4432, 50, 1000);
		System.out.println(acc1);
		System.out.println(acc2);
		System.out.println(acc3);
	}
}
