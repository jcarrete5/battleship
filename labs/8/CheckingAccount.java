public class CheckingAccount extends Account {

	private double overdraftLimit;

	public CheckingAccount(int id, double initBal, double overdraftLimit) {
		super(id, initBal);
		this.overdraftLimit = overdraftLimit;
	}

	@Override
	public void withdraw(double amount) {
		if (getBalance() - amount < -overdraftLimit) throw new IllegalArgumentException("amount is too much");
		super.withdraw(amount);
	}

	@Override
	public String toString() {
		return String.format(
				"CheckingAccount: {id: %d, balance: %f, annualInterestRate: %f, dateCreated: %s, overdraftLimit: %f}",
				getId(), getBalance(), getAnnualInterestRate(), getDateCreated(), overdraftLimit);
	}
}
