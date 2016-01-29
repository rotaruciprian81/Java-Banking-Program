# Java-Banking-Program
This is a banking program developed using Java and Netbeans. The program will allow bank employee to create bank accounts, withdraw and deposit money, give loans, etc

Menu class

	This is the main class of the program and contains all the logic. This class has been broken down into smaller methods in order to make the code more readable and easier to maintain. 
	The methods description are:
-	printMenu – printing the menu to the user
-	main_menu – starting method of this class
-	withdraw – withdraw the specific amount from the accounts
-	createAccount – create a new account
-	deposit – deposit money to the account
-	displayBalance – display the current balance of the account
-	transaferMoney – transfer money from one account to another
-	payInterest – pay interest amount from bank account to all accounts on every 1st of the month
-	addAccountHolder – add new holder to existing account
-	displayAllAccounts – display all accounts that the bank holds for a specific customer
-	ViewTransactions – view all transactions of a specific account
-	modifyOverdraft – modify the overdraft limit of the bank
A bank account it is created in order to pay all the interest from it. This account has been set up as account number 0.  A menu will be printed to the user in order to access all the actions desired. In order to satisfy the customer requirements, in the method that creates accounts 4 new types of accounts have been added: High Interest Accounts, Islamic Bank account, Private Account and low credit rating account. In case of withdrawing from an account and there is insufficient money into it, an error message will be displayed. In case of an operation that tries to access an account and that account number doesn’t exist also an error message will be printed on screen.
A serialization concept has been used to store the accounts and their transaction history. Next time that the application will be started again it will load the same data. This way the bank will be able to store all the accounts and the transaction done. In order to do this the Menu class has started implementing the Serializable interface. All the menu options that need to check for an existing account will display and error message if it will not be found. 
Another method added it is the withdraw one. This method will check the user input and check the account if has enough money in order to finish the transaction. If not, it will display a message that there are insufficient funds. In case that the account holds enough funds a message that confirms the transaction will be printed on the screen.
A third method used will handle the deposit into accounts. First will take the user input and check if the account exists. The “for” loop used in the old code has been replaced and a newer Java implementation have been used. 
Another method used is for displaying the balance on a certain account. Again will take user input and check if the accounts exist. Again the “for” loop have been replaced. 
	In order to transfer money from one account to another, another method had been developed. It will take user input and also validate. It will check if the accounts exist and also if sufficient amount exist which will be debited. 
	In order to take interest a new method has been created. This will be done considering the interest rate and balance on the account. As has been specified, a bank account which is held by the bank has been created with index 0. This account has been created to be debited and pay the interest to the customers. All the accounts held in the program memory will be checked but the one with index 0 will be skipped. For demonstration purposes a set period of 1 minute has been introduced.
	Considering the client requirements an option to add another holder to an account has been created. Again will take user input like account number and new account holder name. If the account exist it will add the requested holder. If not will display an error message.
	A method to display all the accounts held by a certain customer has been added. It will take user input as the first and last name and will check the memory for those. If found it will display. 
	Another method added in order to satisfy the bank requirements has been added to let the user give loan. The method will take the user input as account number and will check if the account exists. This method also restricts the accounts to have more than one account. It will also access the user for the period of the loan to be given. The restriction to give another loan will be in place for that period. Also the bank account will debit that account for the loan payment and also interest. A message to notify about that will be displayed.
	A method to add and modify overdraft has been also being added. It will take user input and check if the account exists. In case that the overdraft has been enabled for that specific account, a limit can be set. 
	
	
