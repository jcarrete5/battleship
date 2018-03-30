import java.util.Date;

public class Account {

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

	@Override
	public String toString() {
		return String.format("Account: {id: %d, balance: %f, annualInterestRate: %f, dateCreated: %s}",
				id, balance, annualInterestRate, dateCreated);
	}
}