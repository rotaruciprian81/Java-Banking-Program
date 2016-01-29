/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication15;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * The main class containing all logic.
 * Here are method description
 * printMenu - print the menu to the user
 * withdraw - withdraw the specific money from account
 * createAccount - create a new account
 * deposit- deposit money to the account
 * displayBalance- display current balance of account
 * transferMoney - transfer money from one account to another
 * payInterest - pay interest amount from bank account to all accounts on every 1st day of month
 * addAccountHolder- add new holder to existing  account
 * displayAllAcounts - display all  accounts bank has
 * ViewTransactions - view all transaction of the  account
 * modifyOverdraft - modify the overdraft limit of the bank.
 * main_menu - starting method of this class
 * @author Radu Ciprian Rotaru
 *
 */
//Implementing the serializable interface to make sure this class object can be persisted
public class Menu implements Serializable{
	
	//no need to store this object; thus marking it transient
	private transient static Scanner input = new Scanner(System.in);
	//List to store all account opened in the bank
	private ArrayList<BaseAccount> accounts = null;
	//account number counter. Always start with 1
	private int accountNumCounter = 0;
		
	public Menu() {
		//creating blank account list
		accounts = new ArrayList<BaseAccount>();
		//adding the bank account to the account list
		accounts.add(new BankAccount("Bank Account", 0, 0));
	}
	/**
	 * print the menu to user. It would display all bank operations allowed and based on user input 
	 * option flow would be executed
	 */
	private void printMenu(){
		System.out.println("\n\n1. Create New Account");
		System.out.println("2. Deposit");
		System.out.println("3. Display Balance");
		System.out.println("4. Withdraw");
		System.out.println("5. Transfer Money");
		System.out.println("6. Add Account Holder");
		System.out.println("7. Show all accounts");
		System.out.println("8. View Transactions");
		System.out.println("9. Overdraft");
		System.out.println("10. Loan");
		System.out.println("0. Exit ");
		System.out.println("Select option: ");
	}
	/**
	 * withdraw the requested money from the account. Proper validation has been taken like
	 * account balance should be more than requested withdraw money. Also as we have overdraft limit functionality;
	 * so if enabled for the account overdraft limit is taken into consideration.
	 */
	private void withdraw(){
		// Write the instruction to the user
		System.out.println("Enter account Number: ");
		// Convert the string the user enters to an int
		int acc_number = Integer.parseInt(input.nextLine());
		// Write instruction to the user
		System.out.println("Enter withdraw amount: ");
		// Convert the string entered by the user to a double
		double amount = Double.parseDouble(input.nextLine());
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number ) {
				boolean balanceSufficient = false;
				//total balance of the account
				double totalAmount = baseAccount.get_balance() +
						(baseAccount.grantOverdraft? baseAccount.overdraftLimit:0);
				//if there is enough balance in the account for withdrawing such amount
				balanceSufficient = totalAmount >= amount;
				//if sufficient balance is there 
				if (balanceSufficient) {
					baseAccount.withdraw(amount);
					System.out.println("Amount "+amount+" withdrawed from account "+acc_number);
				}else{
					System.out.println("Insuffient funds on the account");
				}
				//making true, as there is valid account number exist in bank
				found = true;
				break;
			}
		}
		if(!found){
			System.out.println("Account no "+acc_number+" not found");
		}
	}
	/**
	 * Method to create a new account
	 */
	private void createAccount(){
		System.out.println("1. Current Account");
		System.out.println("2. Savings Account");
		System.out.println("3. Student Account");
		System.out.println("4. Business Account");
		System.out.println("5. SMB Account");
		System.out.println("6. IR Account");
		//Added 4 new types of account which was part of new requirement
		System.out.println("7. High Interest Accounts");
		System.out.println("8. Islamic Bank account");
		System.out.println("9. Private Account");
		System.out.println("10. Low credit rating Account");
		int option2 = Integer.parseInt(input.nextLine());
		System.out.println("Enter first and Last Name");
		String name = input.nextLine();
		System.out.println("Enter Account ID");
		int id = Integer.parseInt(input.nextLine());
		accountNumCounter++;
		//invoking accountFactory to return account object based on choice
		AccountFactory accountFactory = new AccountFactory();
		//Getting account object based on user input. Moved all old switch case to this factory.
		//Using factory pattern
		BaseAccount account = accountFactory.createAccount(option2, name, accountNumCounter, id);
		if(null != account){
			accounts.add(account);
			System.out.println("New Account is created : ");
			System.out.println("Account-No  |Account-Type  |  Holder-Name  |  Balance");
			System.out.println(account);
		}
	}
	/**
	 * Deposit the money. Proper validation has been taken place like input account
	 * number is valid and present in bank
	 */
	private void deposit(){
		// Write the instruction to the user
		System.out.println("Enter account Number: ");
		// Convert the string the user enters to an int
		int acc_number = Integer.parseInt(input.nextLine());
		// Write instruction to the user
		System.out.println("Enter deposit amount: ");
		// Convert the string entered by the user to a double
		boolean found= false;
		double amount = Double.parseDouble(input.nextLine());
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			// if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number ) {
				//crediting the account with the amount
				baseAccount.deposit(amount);
				System.out.println("Amount "+amount +" deposited to account "+acc_number);
				//setting to true; as account number request is valid and present in bank
				found = true;
				break;
			}
		}
		if(!found){
			System.out.println("Account num "+acc_number +" not found");
		}
	}
	/**
	 * Method to display balance of the account. Proper validation is done, like
	 * input account number is valid and exist in bank
	 */
	private void displayBalance(){
		// Write the instruction to the user
		System.out.println("Enter account Number: ");
		// Convert the string the user enters to an int
		int acc_number = Integer.parseInt(input.nextLine());
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if request account number is matched
			if (baseAccount.getAccountNum() == acc_number) {
				//account account balance
				double balance = (baseAccount.get_balance());
				System.out.println("Balance of account:"+acc_number+"  = "+balance);
				//adding the transaction
				baseAccount.addTransaction(new Date(),"View Balance of account:"+acc_number, balance);
				//setting to true; as requested account number is valid and present in bank
				found = true;
				break;
			}
		}
		if(!found){
			System.out.println("Account no does not exist : "+acc_number);
		}
	}
	/**
	 * Method to transfer money from one  account to another. 
	 * Proper validation is done like balance is from account should be more than 
	 * amount asked and input account number should be valid and exist in bank 
	 */
	private void transferMoney() {
		// Write the instruction to the user
		System.out.println("Enter account Number to transfer money FROM:");
		// Convert the string the user enters to an int
		int acc_number1 = Integer.parseInt(input.nextLine());
		System.out.println("Enter account Number to transfer money TO: ");
		// Convert the string the user enters to an int
		int acc_number2 = Integer.parseInt(input.nextLine());
		// Write instruction to the user
		System.out.println("Enter transfer amount: ");
		// Convert the string entered by the user to a double
		double amount = Double.parseDouble(input.nextLine());
		BaseAccount fromAccount = null , toAccount = null;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number1 ) {
				fromAccount = baseAccount;
				break;
			}
		}
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number2 ) {
				toAccount = baseAccount;
				break;
			}
		}
		//if requested from account number does not exist
		if(fromAccount == null){
			System.out.println("From account "+acc_number1+" does not exist");
			return;
		}
		//if requested to account number does not exist
		if(toAccount == null){
			System.out.println("To account "+acc_number2+" does not exist");
			return;
		}
		boolean balanceSufficient = false;
		//get the total balance of the from account
		double totalAmount = fromAccount.get_balance() +
				(fromAccount.grantOverdraft ? fromAccount.overdraftLimit:0);
		//if account balance is sufficient for such withdraw
		balanceSufficient = totalAmount >= amount;
		if (balanceSufficient) {
			fromAccount.withdraw(amount);
		} else {
			System.out.println("There are insufficient funds to make this transfer");
			return;
		}
		toAccount.deposit(amount);
		System.out.println("Payment has been successfully transferred");

	}
	/**
	 * Method to pay the interest to the account based on interest rate and balance in the account.
	 * this has to be done on every 1st of every month. So separate thread is running to keep track of it
	 */
	private void payInterest(){
		if(Calendar.getInstance().get(Calendar.DAY_OF_MONTH) != 1)
			return;
		//we have a logic that bank account is stored at index 0 of the accounts list.
		//We would use this bank account for paying all interest to the users
		BaseAccount bankAccount = accounts.get(0);
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//We are skipping the bank account from here, as we don't need to pay any interest
			//to the bank account. 
			if(baseAccount instanceof BankAccount){
				//Don;t do anything on bank account
			}else	
				baseAccount.interest_rate(bankAccount);
		}
		
	}
	/**
	 * Method to process all loan from all accounts. When time reach, process the monthly payments
	 * If an account has activeLoan field set this mean loan is running in that account
	 */
	private void processLoanPayments(){
		//we have a logic that bank account is stored at index 0 of the accounts list.
		//We would use this bank account for paying all interest to the users
		BaseAccount bankAccount = accounts.get(0);
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//We are skipping the bank account from here, as bank account does not carry any loan
			if(baseAccount instanceof BankAccount){
				//Don't do anything on bank account
			}else	
				baseAccount.processLoanPayments(bankAccount);
		}
	}
	/**
	 * MEthod to add new holder name to existing account.
	 */
	private void addAcountHolder(){
		//enter account number
		System.out.println("Enter Account Number");
		int acc_number = Integer.parseInt(input.nextLine());
		//enter name of account holder
		System.out.println("Enter first and Last Name");
		String name = input.nextLine();
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number  ) {
				//adding new holder name to the account
				baseAccount.AddAccHolder(name);
				System.out.println("new Account holder name "+name +" is added to account "+acc_number);
				//setting to true; is requested account number is valid and present in bank
				found = true;
				break;
			}
		}
		if(!found){
			System.out.println("Account no "+acc_number+" not found" );
		}
	}
	/**
	 * Display all accounts of the holder name. User enter a holder name, all the accounts which are linked
	 * to that name in the bank would be displayed here
	 */
	private void displayAllAccounts(){
		System.out.println("Enter first and Last Name");
		String name = input.nextLine();
		
		System.out.println("Account-No  |Account-Type  |  Holder-Name  |  Balance");
		System.out.println("-----------------------------------------------------");
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//verify if requested holder name present in any account
			 if (baseAccount.isHolderPresent(name)){
				System.out.println(baseAccount);
			}
		}
	}
	/**
	 * View all transaction done on an account. User enter an account number and all transactions
	 * like deposit, withdraw, loan operations occurred in that account would be displayed
	 */
	private void viewTransactions(){
		System.out.println("Enter Account Number");
		int acc_number = Integer.parseInt(input.nextLine());
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number) {
				System.out.println("Transaction-Type  |  Date  |  Amount");
				System.out.println("--------------------------------------------");
				for(Transaction transaction: baseAccount.getTransactions()) {
					System.out.println(transaction);
				}
				//if requested acc num is valid and exist in bank
				found= true;
			}
		}
		if(!found){
			System.out.println("Account no "+ acc_number+" not found");
		}
	}
	/**
	 * Method to issue loan to an account. Proper validation are done like input account number
	 * should be valid and exist in bank. Account should not have already running loan with the bank.
	 * Bank cannot issue 2 loans to a single account
	 */
	private void issueLoan(){
		System.out.println("Enter Account Number");
		int acc_number = Integer.parseInt(input.nextLine());
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if account number is matched
			if (baseAccount.getAccountNum() == acc_number ) {
				//if activeLoan is null; this means no active loan is running and user can take new loan;
				//if not null; then he cannot take new loan, till he pays all monthly installments
				if(baseAccount.activeLoan != null){
					System.out.println("There is already active loan running with amount "+baseAccount.activeLoan.amountLoaned);
					return;
				}
				//Ask the loan amount which you want to issue
				System.out.println("Enter loan amount");
				double loanAmount = Double.parseDouble(input.nextLine());
				//ask for monthly duration of loan
				System.out.println("Enter loan duration in months");
				int loanDuration = Integer.parseInt(input.nextLine());
				//create the loan object
				Loan loan = new Loan(loanDuration,loanAmount,baseAccount.interestRate);
				//add to the loans list
				baseAccount.loans.add(loan);
				//setting to active loan; so that another loan cannot be taken till all
				//monthly payments are made
				baseAccount.activeLoan = loan;
				//adding account with loan amount 
				baseAccount.balance += loanAmount;
				baseAccount.addTransaction(new Date(), "Loan taken by account "+acc_number, loanAmount);
				//fetching the bank account which is stored at index 0
				BaseAccount bankAccount = accounts.get(0);
				//depicting the bank account with loan amount
				bankAccount.balance -= loanAmount;
				bankAccount.addTransaction(new Date(), "Loan provided by bank to account "+acc_number, loanAmount);
				found= true;
			}
		}
		//if requested account number is not  found
		if(!found){
			System.out.println("Account no "+ acc_number+" not found");
		}
	}
	/**
	 * Method to change the overdraft setting of an account. Proper validation has been input like 
	 * input account number should be valid and present in bank.
	 * If overdraft is granted; then one can revoke overdraft or change overdraft limit
	 * If overdraft is revoked; then one can grant overdraft only
	 */
	private void modifyOverdraft(){
		System.out.println("Enter Account Number");
		int acc_number = Integer.parseInt(input.nextLine());
		boolean found = false;
		//Changed  for (int i = 0; i < accounts.size(); i++) to 
		//for (BaseAccount baseAccount: accounts) with newer java implementation
		//It directly give the object 'baseAccount' instead of writing 'accounts.get(i)'
		for (BaseAccount baseAccount: accounts) {
			//if requested account number is matched
			if (baseAccount.getAccountNum() == acc_number ) {
				//display the current setting of overdraft
				System.out.print("Current setting - Overdraft " );
				System.out.println(baseAccount.grantOverdraft?"Enabled":"Disabled");
				//if overdraft is enabled; then display overdraft limit amount
				if(baseAccount.grantOverdraft)
					System.out.println("Overdraft limit  - "+baseAccount.overdraftLimit);
				//show options to user
				System.out.println("1. "+(baseAccount.grantOverdraft?"Revoke Overdraft":"Grant Overdraft"));
				//if overdraft is enabled; then only he can change limit. Else grant overdraft first
				if(baseAccount.grantOverdraft)
					System.out.println("2. Change Overdraft Limit");
				//for any other key, return to main menu
				System.out.println("Press any other integer to go back");
				//asking for user input
				int option2 = Integer.parseInt(input.nextLine());
				switch (option2) {
				case 1:
					//setting the grantoverdraft
					baseAccount.grantOverdraft = !baseAccount.grantOverdraft;
					System.out.print("Overdraft "+(baseAccount.grantOverdraft?"Granted":"Revoked"));
					break;
				case 2:
					//taking user input for overdraft limit amount
					System.out.println("Enter overdraft Limit amount");
					baseAccount.overdraftLimit = Double.parseDouble(input.nextLine());
					break;
				// for any other input, return back
				default:
					break;
				}
				//setting to true; if requested account number is valid and found in bank
				found= true;
			}
		}
		if(!found){
			System.out.println("Account no "+ acc_number+" not found");
		}
		
	}
	/**
	 * Main starting point method of the program. If process all the user input for the bank operations
	 */
	public void main_menu() {
		//start thread for paying interest automatically
		//Thread will run indefinetly; every 1 hour
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					payInterest();
					//sleep thread for 1 minute
					try {
						Thread.sleep(60 *1000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
		
		//start thread for paying loan amount automatically
		//Thread would run indefenitly; every 1 minute for all acounts
		new Thread(new Runnable() {
			@Override
			public void run() {
				while(true){
					processLoanPayments();
					//sleep thread for 1 minute
					try {
						Thread.sleep(60 *1000);
					} catch (InterruptedException e) {}
				}
			}
		}).start();
		
		
		//start menu loop
		while (true) {
			printMenu();
			int option = Integer.parseInt(input.nextLine());
			switch (option) {
			case 1:
				createAccount();
				break;
			//Deposit money
			case 2:
				deposit();
				break;
			//Display Balance
			case 3:
				displayBalance();
				break;
			//Withdraw
			case 4:
				withdraw();
				break;
			//transfer money
			case 5: 
				transferMoney();
				break;
			//add account holder
			case 6:
				addAcountHolder();
				break;
			//show all accounts held by bank
			case 7:
				displayAllAccounts();
				break;
			//display transactions
			case 8:
				viewTransactions();
				break;
			//modify overdraft for an account
			case 9:
				modifyOverdraft();
				break;
			//issue Loan
			case 10:
				issueLoan();
				break;
			default : 
				//saving the all acounts, its loan, transaction to a file
				PersistUtil.getInstance().store(this);
				System.exit(1);
			}
		}
	}
}
