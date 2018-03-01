import java.util.Date;

public class Problem9_7 {

	public static void main(String[] args) {
		Account acct = new Account(1122, 20000);
		acct.setAnnualInterestRate(4.5);
		acct.withdraw(2500);
		acct.deposit(3000);
		System.out.printf("Account balance: $%.2f\n", acct.getBalance());
		System.out.printf("Account monthly interest: $%.2f\n", acct.getMonthlyInterest());
		System.out.println("Account created on " + acct.getDateCreated());
	}

	private static class Account {

		private int id;
		private double balance, annualInterestRate;
		private Date dateCreated;

		public Account() {
			this(0, 0);
		}

		public Account(int id, double initBal) {
			dateCreated = new Date(System.currentTimeMillis());
			this.id = id;
			balance = initBal;
			annualInterestRate = 0;
		}

		public double getMonthlyInterestRate() {
			return annualInterestRate / 12;
		}

		public double getMonthlyInterest() {
			return getMonthlyInterestRate() * balance;
		}

		public void withdraw(double amount) {
			if (amount > balance) throw new IllegalArgumentException("amount is greater than balance");
			balance -= amount;
		}

		public void deposit(double amount) {
			balance += amount;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public double getBalance() {
			return balance;
		}

		public void setBalance(double balance) {
			this.balance = balance;
		}

		public double getAnnualInterestRate() {
			return annualInterestRate;
		}

		public void setAnnualInterestRate(double annualInterestRate) {
			this.annualInterestRate = annualInterestRate / 100;
		}

		public Date getDateCreated() {
			return dateCreated;
		}
	}
}
