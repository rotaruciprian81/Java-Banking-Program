/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Abstract class implementing account interface.
 * It has all basic bank operations implemented like withdraw, deposit,processLoan..
 * 
 * @author Radu Ciprian Rotaru
 *
 */
// Making it abstract as we don't want objects to get created. We can create objects concrete class only
// like currentAccount, savingAccount..
public abstract class BaseAccount implements Account {

	//for tracking balance of the account
	public double balance;
	//to track all holder name of this account. 
	protected ArrayList<String> holders = new ArrayList<String>();
	//to track all transaction done to this account
	protected ArrayList<Transaction> transactions = new ArrayList<Transaction>();
	//interest rate to be charged for any interest payment
	protected double interestRate;
	//account number
	protected int acc_number;
	//type of account
	protected String account_type;
	//account id
	protected int id;
	//value to depict overdraft limit
	protected double overdraftLimit =0;
	//Flag to show if account support overdraft or not. If false, then overdraft is revoked, else granted
	protected boolean grantOverdraft = false;
	//List to store all history loans taken on this account
	protected List<Loan> loans = null;
	//to trace if any active loan is running for which monthly payment is still left out. If null, this
	//mean no loan is running and can entitle new loan
	protected Loan activeLoan = null;
	/**
	 * Set up a new account
	 * 
	 * @param acc_owner
	 * @param acc_num
	 * @param acc_type
	 * @param _id
	 */
	public BaseAccount(String acc_owner, double interestRate, int acc_num, String acc_type, int _id) {
		this.holders.add(acc_owner);
		this.interestRate = interestRate;
		this.acc_number = acc_num;
		this.account_type = acc_type;
		loans = new ArrayList<Loan>();
	}

	/**
	 * Add account holder.
	 * Removed the accountNumber from the method argument as we are invoking the AddAccHolder
	 * method on concrete class object which reflect an account object
	 * 
	 * @param acc_owner
	 * @param acc_num
	 */
	public void AddAccHolder(String acc_owner) {
		holders.add(acc_owner);
	}
	/**
	 * get the account holder name
	 * @return name
	 */
	public String getHolderName() {
		return holders.get(0);
	}
	/**
	 * get the account id
	 * @return id
	 */
	public int getID() {
		return id;
	}
	/**
	 * get the account number
	 * @return account number
	 */
	public int getAccountNum() {
		return acc_number;
	}
	
	/**
	 * Withdraw the amount
	 * @param amount
	 */
	public void withdraw(double amount) {
		//debiting the amount from the account balance
		balance -= amount;
		//recording the transaction
		addTransaction(new Date(),"Withdraw from Account:"+acc_number, amount);
	}

	/**
	 * Deposit the amount
	 * @param amount
	 */
	public void deposit(double amount) {
		//adding the amount to the account balance
		balance += amount;
		//recording the transaction
		addTransaction(new Date(), "Transfer to Account:"+acc_number, amount);
	}
	/**
	 * get the balance of the account
	 * @return balance
	 */
	public double get_balance() {
		return balance;
	}

	/**
	 * Get the account type
	 * @return type
	 */
	public String get_acc_type() {
		return account_type;
	}
	
	
	/**
	 * Method to add the interest to the balance
	 * Added new argument BaseAccount reflecting bank account from which interest
	 * would be withdraw and added to account
	 */
	public void interest_rate(BaseAccount bankAccount) {
		//calculating the interest of the balance
		double interest = interestRate * balance / 100;
		//if interest amount is greater than zero; so bank need to pay the interest
		if(interest > 0){
			//withdraw from bank account
			bankAccount.withdraw(interest);
			//adding a transaction in bank account for recording
			bankAccount.addTransaction(new Date(), "Interest paid to account "+acc_number, interest);
			//add to account
			balance += interest;
			//adding a transaction to the current account
			addTransaction(new Date(), "Interest paid to account", interest);
		}
	}
	/**
	 * Method to process all loan monthly payments
	 * @param bankAccount
	 */
	public void processLoanPayments(BaseAccount bankAccount){
		//if activeLoan is null; means there are no active loan running for that user for which
		//monthly payment need to be made. So just return
		if(null == activeLoan)
			return;
		//fetch out last payment date of the loan. 
		Date lastPaymentDate = null;
		//activeLoan.paymentDateList would be blank if this is first time user is paying its first
		//installment of the new loan. So lastPaymentDate would be null
		if(activeLoan.paymentDateList.size() >0 )
			lastPaymentDate = activeLoan.paymentDateList.get(activeLoan.paymentDateList.size()-1);
		//if this is not first monthly installment, then calculate the minutes passed when actually 
		// last payment was made relative to current time
		long diff, diffMinutes=0;
		if(null != lastPaymentDate){
			diff = new Date().getTime() - lastPaymentDate.getTime();
	    	diffMinutes = diff / (60 * 1000) % 60;
		}
		//for demonstration sake we have assumed monthly payment need to be made every 5 minutes
		//If this is first installment or time gap of last payment made has exceeded 5minutes
	    if(null == lastPaymentDate || diffMinutes >= 5){
	    	//add new payment date to the paymentDateList for recording all payment history
	    	activeLoan.paymentDateList.add(new Date());
	    	//calculate the monthly payment i.e amount to be paid back divided by duration of loan
	    	double monthlyPaymentAmount = activeLoan.amountLoaned / activeLoan.duration;
	    	//add the monthy payment amount to the paymentAmountList
	    	activeLoan.paymentAmountList.add(monthlyPaymentAmount);
	    	//reducing the balance of the account by monthly payment which is credited to bank account
	    	balance -= monthlyPaymentAmount;
	    	//crediting the bank account 
	    	bankAccount.balance += monthlyPaymentAmount;
	    	//adding the transaction to keep track 
			addTransaction(new Date(), "Loan amount debited from the account", monthlyPaymentAmount);
			bankAccount.addTransaction(new Date(), "Loan amount credited to account from account "+acc_number, monthlyPaymentAmount);

	    	System.out.println("Loan monthly payment "+monthlyPaymentAmount +" has been debited from account "+acc_number);
	    	//if account balance has turned less than zero, then alerting user
	    	if(balance <= 0)
	    		System.out.println(holders.get(0)+" ; Please submit balance in your account "+acc_number);
	    	//if all monthly payment have been made; so making activeLoan as null indicating no active loan is running now
	    	//and user is entitled to take new loan
	    	if(activeLoan.paymentAmountList.size() == activeLoan.duration)
	    		activeLoan = null;
	    }
	}

	/**
	 * Add transaction to the list
	 * @param d
	 * @param trans_Type
	 * @param amount
	 */
	public void addTransaction(Date d, String trans_Type, double amount) {
		transactions.add(new Transaction(d, trans_Type, amount));
	}

	/**
	 * Get all transactions of the account
	 * @return
	 */
	public ArrayList<Transaction> getTransactions() {
		return transactions;
	}
	/**
	 * Method to change if holder name is present in the account
	 * @param name
	 * @return
	 */
	public boolean isHolderPresent(String name){
		if(null == name || name.trim().isEmpty())
			return false;
		for(String holder: holders){
			if(holder.startsWith(name))
				return true;
		}
		return false;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getAccountNum() +"  |  "+get_acc_type() + "  |  "+ getHolderName() + "  |  "+ get_balance();
	}
}

/**
 * A class is added to create concrete object based on user input of account type.
 * e.g 1 for currentAccount object, 2 for savingAccount and so on..
 * @author 
 *
 */
class AccountFactory{
	/**
	 * A factory method to generate the factory object based on input
	 * @param option
	 * @param name
	 * @param accountNo
	 * @param id
	 * @return
	 */
	public BaseAccount createAccount(int option, String name, int accountNo, int id){
		BaseAccount account = null;
		switch (option) {
		case 1:
			account = new CurrentAccount(name, accountNo, id);
			break;
		case 2:
			account =new SavingsAccount(name, accountNo, id);
			break;
		case 3:
			account = new StudentAccount(name, accountNo, id);
			break;
		case 4:
			account =new BusinessAccount(name, accountNo, id);
			break;
		case 5:
			account = new SMBAccount(name, accountNo, id);
			break;
		case 6:
			account = new IRAccount(name, accountNo, id);
			break;
		case 7:
			account = new HighInterestAccounts(name, accountNo, id);
			break;
		case 8:
			account =new IslamicBankAccounts(name, accountNo, id);
			break;
		case 9:
			account =new PrivateAccounts(name, accountNo, id);
			break;
		case 10:
			account =new LowCreditRatingAccounts(name, accountNo, id);
			break;
		default: 
			System.out.println("Wrong choice");
			break;
		}
		return account;
	}
}
/**
 * This class reflect the bank concrete account. This is account from which all 
 * interest would be transfered to each  account
 * We have set initial balance of bank account to 1000000000 
 * @author Radu Ciprian Rotaru  
 *
 */
class BankAccount extends BaseAccount{
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public BankAccount(String owner, int acc_num, int _id) {
		super(owner, 9.0,acc_num, "Bank", _id);
		balance = 1000000000;
	}

}

/**
 * Business account concrete class
 * @author 
 *
 */
class BusinessAccount extends BaseAccount {
	/**
	 * 
	 * @param business_name
	 * @param acc_num
	 * @param _id
	 */
	public BusinessAccount(String business_name, int acc_num, int _id) {
		super(business_name ,9.0, acc_num, "Business", _id);
	}
	/**
	 * Withdraw the amount
	 * @param amount
	 */
	@Override
	public void withdraw(double amount) {
		if (amount <= 500) {
			balance -= amount;
			addTransaction(new Date(),"Withdraw from Account:"+acc_number, amount);
		} else {
			System.out.println("The maximum daily withdrawal for a Business account is ?500. This transaction has been cancelled");
		}
		
	}
}

/**
 * Current account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class CurrentAccount extends BaseAccount {
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public CurrentAccount(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "Current", _id);
	}
	/**
	 * Withdraw the amount
	 * @param amount
	 */
	@Override
	public void withdraw(double amount) {
		if (amount <= 500) {
			balance -= amount;
			addTransaction(new Date(),"Withdraw from Account:"+acc_number, amount);
		} else {
			System.out.println("The maximum daily withdrawal for a Business account is ?500. This transaction has been cancelled");
		}
		
	}
}

/**
 * IA account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class IRAccount extends BaseAccount {
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public IRAccount(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "IR", _id);
	}

}

/**
 * Student account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class StudentAccount extends BaseAccount {
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public StudentAccount(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "Student", _id);
	}

}

/**
 * SMB account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class SMBAccount extends BaseAccount {
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public SMBAccount(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "SMB", _id);
	}

}

/**
 * Saving account concrete class
 * @author radu Ciprian Rotaru
 *
 */
class SavingsAccount extends BaseAccount {
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public SavingsAccount(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "Savings", _id);
	}
	/**
	 * Withdraw the amount
	 * @param amount
	 */
	@Override
	public void withdraw(double amount) {
		if (amount <= 300) {
			balance -= amount;
			addTransaction(new Date(),"Withdraw from Account:"+acc_number, amount);
		} else {
			System.out.println("The maximum daily withdrawal for a Savings account is ?300. This transaction has been cancelled");
		}
		
	}

}

/**
 * Private account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class PrivateAccounts extends BaseAccount{
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public PrivateAccounts(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "Current", _id);
	}

}

/**
 * High Interest account concrete class
 * In this type of bank accounts; one can avail high interest charges
 * @author radu Ciprian Rotaru
 *
 */
class HighInterestAccounts extends BaseAccount{
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public HighInterestAccounts(String owner, int acc_num, int _id) {
		super(owner,11.0, acc_num, "HighInterest", _id);
	}
	
}
/**
 * Islamic Bank account concrete class
 * In this type of bank account; one would not be able to avail interest benefit
 * @author Radu Ciprian Rotaru
 *
 */
class IslamicBankAccounts extends BaseAccount{
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public IslamicBankAccounts(String owner, int acc_num, int _id) {
		super(owner,0.0, acc_num, "IslamicBank", _id);
	}
	/* (non-Javadoc)
	 * @see BaseAccount#interest_rate(BaseAccount)
	 */
	@Override
	public void interest_rate(BaseAccount bankAccount) {
		//Do nothing as no interest should be charged to this account
	}
}
/**
 * Low credit ratings account concrete class
 * @author Radu Ciprian Rotaru
 *
 */
class LowCreditRatingAccounts extends BaseAccount{
	/**
	 * 
	 * @param owner
	 * @param acc_num
	 * @param _id
	 */
	public LowCreditRatingAccounts(String owner, int acc_num, int _id) {
		super(owner,9.0, acc_num, "LowCreditRating", _id);
	}

}
