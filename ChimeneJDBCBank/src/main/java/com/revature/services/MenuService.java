package com.revature.services;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.revature.services.LedgerService;
import com.revature.beans.*;

public class MenuService {
	private static Scanner scan;
	private static LedgerService ledger;
	private static User currentUser;
	private static boolean isEmployee;
	private static final Logger LOGGER = LogManager.getLogger(MenuService.class);

	public MenuService() {
		if (scan == null) {
			scan = new Scanner(System.in);
			ledger = new LedgerService();
			currentUser = null;
			isEmployee = false;
		}
	}
	
	public void initializeMenu() {
		ledger.initializeLedger();
		LOGGER.debug("Ledger Initialized");
	}

	public int entryPoint() {
		int choice = 0;
		boolean done = false;
		while (!done) {
			System.out.println("Welcome to the JDBC Bank Application. \nTo login to an existing account, enter 1\n"
					+ "To create a new user, enter 2");
			try {
				choice = scan.nextInt();
				if (choice == 1 || choice == 2) {
					done = true;
				} else {
					scan.nextLine();
				}
			} catch (InputMismatchException e) {
				scan.nextLine();
			}
		}
		return choice;
	}

	public boolean login() {
		User user = null;
		boolean done = false;
		String username = null;
		scan.nextLine();

		while (!done) {
			System.out.println("Please enter your username");
			username = scan.nextLine();
			if (username.toLowerCase().equals("back")) {
				return false;
			}
			user = ledger.getUser(username.toLowerCase());
			if (user == null) {
				System.out.println("That username does not exist.");
				LOGGER.warn("User entered a username not in the ledger");
			} else {
				done = true;
			}
		}
		done = false;
		while (!done) {
			System.out.println("Please enter your password");
			String password = scan.nextLine();
			if (password.equals(user.getPassword())) {
				currentUser = user;
				isEmployee = user.isEmployee();
				LOGGER.info(currentUser.getUsername() + " has logged in");
				return true;
			} else if (password.toLowerCase().equals("back")) {
				return false;
			} else {
				System.out.println("Username and password do not match");
				LOGGER.warn("User entered a password that did not match the username");
			}
		}

		return false;
	}

	public boolean register() {
		boolean done = false;
		String username = null;
		String password = null;
		boolean emp = false;
		scan.nextLine();

		while (!done) {
			System.out.println("Are you an employee? Y or N");
			String answer = scan.nextLine();
			if (answer.toLowerCase().equals("y")) {
				emp = true;
				done = true;
			} else if (answer.toLowerCase().equals("n")) {
				emp = false;
				done = true;
			} else if (answer.toLowerCase().equals("back")) {
				return false;
			} else {
				scan.nextLine();
				LOGGER.warn("User failed to identify whether they were an employee (Unexpected input)");
			}
		}
		done = false;

		while (!done) {
			System.out.println("Please enter a username");
			username = scan.nextLine();
			if (username.toLowerCase().equals("back")) {
				return false;
			} else if (username.length() > 0 && ledger.getUser(username) == null) {
				LOGGER.debug("User successfully entered a unique username");
				done = true;
			} else {
				System.out.println("That username has been taken");
				LOGGER.warn("User entered a username already in the ledger or some other unexpected input");
			}
		}
		done = false;
		while (!done) {
			System.out.println("Please enter a password");
			password = scan.nextLine();
			if (password.length() > 0) {
				LOGGER.debug("User successfully entered a valid password");
				done = true;
			} else {
				scan.nextLine();
				LOGGER.warn("User attempted to enter an empty password field");
			}
		}

		ledger.addUser(emp, username, password);
		currentUser = ledger.getUser(username);
		isEmployee = emp;
		LOGGER.info(username + " has successfully registered a new account");
		return true;
	}

	public int mainMenu() {
		boolean done = false;
		String answer = null;
		int num = 0;

		System.out.println("Welcome, " + currentUser.getUsername());

		// Employee menu
		if (isEmployee) {
			while (!done) {
				System.out.println(
						"Enter the number of an option below to continue\n1. View personal accounts\n2. Make a withdrawal"
								+ "\n3. Make a deposit\n4. Transfer funds between accounts\n5. Create a new account"
								+ "\n6. View pending accounts \n7. Cancel an account\n8. Lookup Customer Information"
								+ "\n9. Create new user\n10. Update user\n11. Remove user\n12. View personal transaction history \n13. Log out\n14. Exit");
				try {
					num = scan.nextInt();
					if (num > 0 && num < 12) {
						done = true;
					} else if (num == 12) {
						return 13;
					} else if (num == 13) {
						return -1;
					} else if (num == 14) {
						return -1;
					} else {
						LOGGER.warn("Employee entered an unexpected value at the main menu");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					LOGGER.warn("Employee entered an unexpected value at the main menu");
					scan.nextLine();
				}
			}
		} // New customer menu
		else if (currentUser.getAccounts().size() == 0) {
			while (!done) {
				System.out.println("You currently have no open accounts. Would you like to open a new one? Y or N");
				answer = scan.nextLine();
				if (answer.toLowerCase().equals("y")) {
					num = openAccount();
					LOGGER.info("New customer attempted to open a new account");
					if (num > 0) {
						LOGGER.debug("The system failed to open the new account or the process was canceled");
						continue;
					} else {
						LOGGER.debug("The system opened the new account successfully");
						return num;
					}
				} else if (answer.toLowerCase().equals("n")) {
					LOGGER.info("New customer chose not to open a new account");
					System.out.println("Good bye");
					return -1;
				}
			}
		} // Regular customer menu
		else {
			while (!done) {
				System.out.println(
						"Enter the number of an option below to continue\n1. View accounts\n2. Make a withdrawal"
								+ "\n3. Make a deposit\n4. Transfer funds between accounts\n5. Create a new account"
								+ "\n6. Close empty account\n7. View transaction history\n8. Log out\n9. Exit");
				try {
					num = scan.nextInt();
					if (num > 0 && num < 6) {
						done = true;
					} else if (num == 6) {
						return 12;
					} else if (num == 7) {
						return 13;
					} else if (num == 8) {
						return 0;
					} else if (num == 9) {
						return -1;
					} else {
						LOGGER.debug("Customer entered an unexpected value at the main menu");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					LOGGER.debug("Customer entered an unexpected value at the main menu");
					scan.nextLine();
				}
			}
		}

		return num;
	}

	public int openAccount() {
		boolean done = false;
		ArrayList<String> names = new ArrayList<>();
		String answer = null;
		int index;
		String name = null;
		scan.nextLine();

		LOGGER.info("New account menu successfully opened");

		names.add(currentUser.getUsername());
		while (!done) {
			System.out.println("Is this an individual account? Y or N");
			answer = scan.nextLine();
			if (answer.toLowerCase().equals("back")) {
				return 1;
			} else if (answer.toLowerCase().equals("y")) {
				ledger.createAcc(names);
				LOGGER.debug("User created an individual account");
				System.out.println("Thank you, your account application has been submitted for employee review.");
				return -1;
			} else if (answer.toLowerCase().equals("n")) {
				while (!done) {
					System.out.println("Please enter the usernames of the other account holders separated "
							+ "by commas\nEX: John Doe,Jane Doe,Mary Sue");
					answer = scan.nextLine();
					if (answer.toLowerCase().equals("back")) {
						break;
					}
					index = 0;
					while (answer.indexOf(",") > 0) {
						name = answer.substring(index, answer.indexOf(","));
						System.out.println("Name: " + name);
						if (ledger.getUser(name) != null) {
							names.add(name);
							index = answer.indexOf(",") + 1;
							answer = answer.substring(index);
							index = 0;
						} else {
							LOGGER.warn("Failed to parse accounts while creating a joint account");
							System.out.println(name + " is not a valid username");
							break;
						}
					}
					if (ledger.getUser(name) == null) {
						continue;
					} else if (ledger.getUser(answer) != null) {
						names.add(answer);
						ledger.createAcc(names);
						LOGGER.debug("User created a joint account");
						System.out
								.println("Thank you, your account application has been submitted for employee review.");
						return -1;
					} else {
						LOGGER.warn("Failed to parse accounts while creating a joint account");
						System.out.println(answer + " is not a valid username");
					}
				}
			}
		}

		return 0;
	}

	public void accountsMenu() {
		boolean done = false;
		Account acc;

		LOGGER.info("User opened the view accounts menu");

		if (currentUser.getAccounts().size() == 0) {
			System.out.println("You currently have no open accounts.");
			LOGGER.debug("User had no open accounts");
		} else {
			for (int i : currentUser.getAccounts()) {
				acc = ledger.getAccount(i);
				StringBuilder str = new StringBuilder();
				str.append("(" + acc.getAID()  +")");
				if(acc.isPending()) {
					str.append("Pending ");
				}
				int size = acc.getUsers().size();
				if(size == 1) {
					str.append("Individual Account ");
				}
				else if(size > 1) {
					str.append("Joint Account held by ");
					for(int j=0; j<size-1; j++) {
						str.append(ledger.getName(acc.getUsers().get(j)) + ", ");
					}
					str.append("and " + acc.getUsers().get(size-1) + " ");
				}
				str.append("that contains $" + acc.getAmount());
				System.out.println(str);
			}
			LOGGER.debug("User accounts successfully displayed");
		}
		System.out.println("Enter \"done\" to return to the main menu");

		while (!done) {
			String temp = scan.nextLine();
			if (temp.toLowerCase().equals("done")) {
				LOGGER.debug("Exiting to main menu");
				done = true;
			}
		}
	}

	public void transactionMenu(boolean isWithdrawal) {
		String transaction = isWithdrawal ? "withdraw from" : "deposit to";
		boolean done = false;
		int num = 0;
		int amount = 0;
		String temp = null;
		Account acc = null;
		scan.nextLine();

		LOGGER.info("User opened the menu to " + transaction + " an account");

		while (!done) {
			while (!done) {
				System.out.println("Please enter the id of the account you wish to " + transaction);
				try {
					num = scan.nextInt();
					acc = ledger.getAccount(num);
					if (acc != null && !acc.isPending() && (isEmployee
							|| (!isEmployee && acc.getUsers().indexOf(currentUser.getUid()) > -1))) {
						LOGGER.debug("Successfully looked up account " + acc.getAID() + " that user wished to access");
						done = true;
					} else {
						if (acc == null) {
							System.out.println("Account not found");
							LOGGER.debug("Account lookup failed (ID not in the ledger)");
						} else {
							LOGGER.debug("User attempted to access account " + acc.getAID() + " which is pending or not their own");
							System.out.println(
									"You do not have access permission for this account or this account is still pending");
						}
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					} else {
						LOGGER.warn("User failed to specify account during transaction (Unexpected input)");
					}
				}
			}
			done = false;
			while (!done) {
				System.out.println("Enter the amount you wish to " + transaction + " this account");
				try {
					amount = scan.nextInt();
					if (!isWithdrawal && amount > 0) {
						ledger.deposit(num, amount);
						System.out.println("Successfully deposited $" + amount + " into account " + num);
						LOGGER.debug("Deposit completed: $" + amount + " deposited into account" + num);
						done = true;
					} else if (isWithdrawal && ledger.getAccount(num).getAmount() >= amount && amount > 0) {
						ledger.withdraw(num, amount);
						System.out.println("Successfully withdrew $" + amount + " from account " + num);
						LOGGER.debug("Withdrawal completed: $" + amount + " withdrawn from account" + num);
						done = true;
					} else {
						if(amount<0) {
							System.out.println("Invalid amount: negative number");
						}
						else {
							System.out.println("There are insufficient funds to withdraw $" + amount + " from this account");
						}
						LOGGER.debug("Transaction amount specified an invalid integer (Either too large or negative)");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
					else {
						LOGGER.warn("User failed to specify amount during transaction (Unexpected input)");
					}
				}
			}
			done = false;

			while (!done) {
				System.out.println("Do you wish to " + transaction + " another account? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("back") || (temp.toLowerCase().equals("n"))) {
					return;
				} else if (temp.toLowerCase().equals("y")) {
					LOGGER.info("User chose to make another transaction");
					break;
				}
			}
		}
	}

	public void transferMenu() {
		boolean done = false;
		int accF = 0;
		int accT = 0;
		String temp = null;
		Account acc = null;
		int amount = 0;
		scan.nextLine();
		
		LOGGER.info("User successfully opened the transfer menu");

		while (!done) {
			while (!done) {
				System.out.println("Please enter the id of the account you wish to transfer from");
				try {
					accF = scan.nextInt();
					acc = ledger.getAccount(accF);
					if (acc != null && !acc.isPending() && (isEmployee
							|| (!isEmployee && acc.getUsers().indexOf(currentUser.getUid()) > -1))) {
						LOGGER.debug("Successfully looked up account " + acc.getAID() + " that user wished to transfer from");
						done = true;
					} else {
						if (acc == null) {
							LOGGER.debug("Account lookup failed (ID not in the ledger)");
							System.out.println("Account not found");
						} else {
							LOGGER.debug("User attempted to access account " + acc.getAID() + " which is pending or not their own");
							System.out.println("You do not have access permission for this account");
						}
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
					else {
						LOGGER.warn("User failed to specify account during transaction (Unexpected input)");
					}
				}
			}
			done = false;
			while (!done) {
				System.out.println("Please enter the id of the account you wish to transfer to");
				try {
					accT = scan.nextInt();
					if (accT != accF && ledger.getAccount(accT) != null && !ledger.getAccount(accT).isPending()) {
						LOGGER.debug("Successfully looked up account " + acc.getAID() + " that user wished to transfer to");
						done = true;
					} else {
						LOGGER.debug("Account lookup failed (ID not in the ledger or account is pending)");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
					else {
						LOGGER.warn("User failed to specify account during transaction (Unexpected input)");
					}
				}
			}
			done = false;
			while (!done) {
				System.out.println("Enter the amount you wish to transfer.");
				try {
					amount = scan.nextInt();
					if (amount > 0 && acc.getAmount() > amount) {
						ledger.transfer(accF, accT, amount);
						LOGGER.debug("Transaction completed: $" + amount + " from account " + accF + " to account " + accT);
						System.out.println("Successfully transferred $" + amount + " from account " + accF + " to account " + accT);
						done = true;
					} else {
						if(amount<0) {
							System.out.println("Invalid amount: negative number");
						}
						else {
							System.out.println("There are insufficient funds to withdraw $" + amount + " from that account");
						}
						LOGGER.debug("Transfer amount specified an invalid integer (Either too large or negative)");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
					else {
						LOGGER.warn("User failed to specify account during transaction (Unexpected input)");
					}
				}
			}
			done = false;

			while (!done) {
				System.out.println("Do you wish to make any other transfers? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("back") || (temp.toLowerCase().equals("n"))) {
					return;
				} else if (temp.toLowerCase().equals("y")) {
					LOGGER.info("User chose to make another transfer");
					break;
				}
			}
		}
	}

	public void customerLookup() {
		boolean done = false;
		String answer = null;
		User user = null;
		scan.nextLine();

		while (!done) {
			System.out.println("Please enter the username of the customer you want to lookup");
			answer = scan.nextLine();
			if (answer.toLowerCase().equals("back")) {
				return;
			} else if ((user = ledger.getUser(answer)) != null) {
				System.out.println(user);
				LOGGER.info("User succesfully looked up " + user.getUsername());
				for (int i : user.getAccounts()) {
					System.out.println(ledger.getAccount(i));
				}
				while (!done) {
					System.out.println("Would you like to lookup another customer? Y or N");
					answer = scan.nextLine();
					if (answer.toLowerCase().equals("back") || (answer.toLowerCase().equals("n"))) {
						return;
					} else if (answer.toLowerCase().equals("y")) {
						break;
					}
				}
			} else {
				System.out.println("User not found");
				LOGGER.debug("User failed to specify user during lookup (not in ledger)");
			}
		}
	}

	public void pendingAccMenu() {
		boolean done = false;
		int num = 0;
		String temp = null;
		LOGGER.info("User viewed pending accounts");

		for (Account acc : ledger.getPendingAcc()) {
			System.out.println(acc);
		}
		while (!done) {
			while (!done) {
				System.out.println("Please enter the id of the account you wish to approve or reject");
				try {
					num = scan.nextInt();
					if (ledger.getAccount(num) != null  && ledger.getAccount(num).isPending()) {
						done = true;
					} else {
						System.out.println("Account not found or is not pending");
						LOGGER.debug("User entered an invalid account in the pending account menu");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					LOGGER.warn("User failed to specify account in pending account menu (Unexpected input)");
					if (temp.toLowerCase().equals("back")) {
						return;
					}
				}
			}
			done = false;
			scan.nextLine();

			while (!done) {
				System.out.println("Do you wish to approve or reject \"" + ledger.getAccount(num) + "\"? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("back")) {
					return;
				} else if (temp.toLowerCase().equals("approve") || temp.toLowerCase().equals("y")) {
					ledger.approveAcc(num);
					LOGGER.debug("User approved account " + num);
					break;
				} else if (temp.toLowerCase().equals("reject") || temp.toLowerCase().equals("n")) {
					ledger.cancelAcc(num);
					LOGGER.debug("User rejected account " + num);
					break;
				}
			}

			while (!done) {
				System.out.println("Do you wish to approve or reject any other accounts? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("back") || (temp.toLowerCase().equals("n"))) {
					return;
				} else if (temp.toLowerCase().equals("y")) {
					break;
				}
			}
		}
	}

	public void cancelAccMenu() {
		boolean done = false;
		int num = 0;
		String temp = null;
		LOGGER.info("User opened the cancel account menu");
		
		while (!done) {
			while (!done) {
				System.out.println("Please enter the id of the account you wish to cancel");
				try {
					num = scan.nextInt();
					if (ledger.getAccount(num) != null) {
						done = true;
					} else {
						System.out.println("Account not found");
						LOGGER.debug("User entered an invalid account in the cancel account menu");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
					else {
						LOGGER.warn("User failed to specify account in cancel account menu (Unexpected input)");
					}
				}
			}
			done = false;
			scan.nextLine();
			while (!done) {
				System.out.println("Are you sure you wish to cancel \"" + ledger.getAccount(num) + "\"? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("y")) {
					ledger.cancelAcc(num);
					LOGGER.debug("User canceled account " + num);
					break;
				} else if (temp.toLowerCase().equals("n")) {
					break;
				} else if (temp.toLowerCase().equals("back")) {
					return;
				}
			}

			while (!done) {
				System.out.println("Do you wish to cancel any other accounts? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("back") || (temp.toLowerCase().equals("n"))) {
					return;
				} else if (temp.toLowerCase().equals("y")) {
					break;
				}
			}
		}
	}
	
	public void updateUser() {
		String username;
		String old = "";
		boolean done = false;
		User user = new User();
		String password;
		
		while (!done) {
			System.out.println("Please enter the username of the user you wish to update");
			username = scan.nextLine();
			if (username.toLowerCase().equals("back")) {
				return;
			}
			user = ledger.getUser(username.toLowerCase());
			if (user == null) {
				System.out.println("That username does not exist.");
				LOGGER.warn("User entered a username not in the ledger");
			} else {
				old = username;
				done = true;
			}
		}
		done = false;
		user = new User();
		
		while (!done) {
			System.out.println("Please enter a new username");
			username = scan.nextLine();
			if (username.toLowerCase().equals("back")) {
				return;
			} else if (username.length() > 0 && ledger.getUser(username) == null && username.length()<20) {
				LOGGER.debug("User successfully entered a unique username");
				user.setUsername(username);
				done = true;
			} else {
				System.out.println("That username has been taken");
				LOGGER.warn("User entered a username already in the ledger or some other unexpected input");
			}
		}
		done = false;
		while (!done) {
			System.out.println("Please enter a new password");
			password = scan.nextLine();
			if (password.length() > 0 && password.length()<20) {
				LOGGER.debug("User successfully entered a valid password");
				user.setPassword(password);
				done = true;
			} else {
				scan.nextLine();
				LOGGER.warn("User attempted to enter an empty password field");
			}
		}
		
		ledger.updateUser(user, old);
		System.out.println("Succesfully updated account details");
	}
	
	public void removeUser() {
		String username = "";
		boolean done = false;
		User user = new User();
		
		
		while (!done) {
			System.out.println("Please enter the username of the user you wish to update");
			username = scan.nextLine();
			if (username.toLowerCase().equals("back")) {
				return;
			}
			user = ledger.getUser(username.toLowerCase());
			if (user == null) {
				System.out.println("That username does not exist.");
				LOGGER.warn("User entered a username not in the ledger");
			} else {
				done = true;
			}
		}
		
		ledger.removeUser(username);
		System.out.println("Succesfully removed user: " + username);
	}
	
	public void cancelEmptyAccount() {
		User u = ledger.getUser(currentUser.getUsername());
		Account acc;
		ArrayList<Integer> emptyAccs = new ArrayList<Integer>();
		boolean done = false;
		int num = 0;
		String temp = "";
		
		for(Integer i : u.getAccounts()) {
			acc = ledger.getAccount(i);
			if(acc.getAmount() == 0) {
				System.out.println(acc);
				emptyAccs.add(i);
			}
		}
		
		if(emptyAccs.size() == 0) {
			System.out.println("You have no empty accounts");
		}
		else {
			while (!done) {
				while (!done) {
					System.out.println("Please enter the id of the account you wish to cancel");
					try {
						num = scan.nextInt();
						if (ledger.getAccount(num) != null) {
							done = true;
						} else {
							System.out.println("Account not found");
							LOGGER.debug("User entered an invalid account in the cancel account menu");
							scan.nextLine();
						}
					} catch (InputMismatchException e) {
						temp = scan.nextLine();
						if (temp.toLowerCase().equals("back")) {
							return;
						}
						else {
							LOGGER.warn("User failed to specify account in cancel account menu (Unexpected input)");
						}
					}
				}
				done = false;
				scan.nextLine();
				while (!done) {
					System.out.println("Are you sure you wish to cancel \"" + ledger.getAccount(num) + "\"? Y or N");
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("y")) {
						ledger.cancelAcc(num);
						LOGGER.debug("User canceled account " + num);
						break;
					} else if (temp.toLowerCase().equals("n")) {
						break;
					} else if (temp.toLowerCase().equals("back")) {
						return;
					}
				}

				while (!done) {
					System.out.println("Do you wish to cancel any other accounts? Y or N");
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back") || (temp.toLowerCase().equals("n"))) {
						return;
					} else if (temp.toLowerCase().equals("y")) {
						break;
					}
				}
			}
		}
		
	}
	
	public void transactionList() {
		User u = ledger.getUser(currentUser.getUsername());
		ArrayList<Transaction> transList = new ArrayList<Transaction>();
		boolean done = false;
		String temp = "";
		
		
		for(Integer i : u.getAccounts()) {
			if(!ledger.getAccount(i).isPending()) {
				transList = ledger.getTransactionList(i);
			}
			System.out.println("Account #" + i);
			if(transList == null || transList.size() == 0) {
				System.out.println("This account has no transaction history");
			} else {
				for(Transaction t : transList) {
					System.out.println(t);
				}
			}
		}
		
		System.out.println("Enter \"done\" to return to the main menu");

		while (!done) {
			temp = scan.nextLine();
			if (temp.toLowerCase().equals("done")) {
				LOGGER.debug("Exiting to main menu");
				done = true;
			}
		}
	}

}
