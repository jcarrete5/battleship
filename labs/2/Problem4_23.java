import java.text.NumberFormat;
import java.util.Locale;
import java.util.Scanner;

public class Problem4_23 {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter employee's name: ");
		String empName = in.nextLine();
		System.out.print("Enter number of hours worked in a week: ");
		int hoursWorked = in.nextInt();
		System.out.print("Enter hourly pay rate: ");
		double hourlyPayRate = in.nextDouble();
		System.out.print("Enter federal tax withholding rate: ");
		double fedTax = in.nextDouble();
		System.out.print("Enter state tax withholding rate: ");
		double stateTax = in.nextDouble();
		in.close();

		double grossPay = hourlyPayRate * hoursWorked;
		NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
		System.out.println("Employee Name: " + empName);
		System.out.println("Hours Worked: " + hoursWorked);
		System.out.println("Pay Rate: " + currency.format(hourlyPayRate));
		System.out.println("Gross Pay: " + currency.format(grossPay));
		System.out.println("Deductions:");
		System.out.println("\tFederal Withholding (" + (fedTax * 100) + "%): "
				+ currency.format(fedTax * grossPay));
		System.out.println("\tState Withholding (" + stateTax * 100 + "%): "
				+ currency.format(stateTax * grossPay));
		System.out.println("\tTotal Deduction: "
				+ currency.format(fedTax * grossPay + stateTax * grossPay));
		System.out.println("Net Pay: "
				+ currency.format(grossPay - (fedTax * grossPay + stateTax * grossPay)));
	}
}
