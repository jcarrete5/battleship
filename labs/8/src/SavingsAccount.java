public class SavingsAccount extends Account {

	public SavingsAccount(int id, double initBal) {
		super(id, initBal);
	}

	@Override
	public void withdraw(double amount) {
		if (amount > getBalance()) throw new IllegalArgumentException("amount is greater than balance");
		super.withdraw(amount);
	}

	@Override
	public String toString() {
		return String.format("SavingsAccount: {id: %d, balance: %f, annualInterestRate: %f, dateCreated: %s}",
				getId(), getBalance(), getAnnualInterestRate(), getDateCreated());
	}
}
