package com.revature.menu;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.LoggerConfig;

import com.revature.engine.LedgerEngine;
import com.revature.user.Account;
import com.revature.user.User;

public class Menu {
	private static Scanner scan;
	private static LedgerEngine ledger;
	private static User currentUser;
	private static boolean isEmployee;
	private static final Logger LOGGER = LogManager.getLogger(Menu.class);

	public Menu() {
		if (scan == null) {
			scan = new Scanner(System.in);
			ledger = new LedgerEngine();
			currentUser = null;
			isEmployee = false;
		}
	}

	public int entryPoint() {
		int choice = 0;
		boolean done = false;
		ledger.initializeLedger();
		LOGGER.debug("Ledger Initialized");
		while (!done) {
			System.out.println("Welcome to the Bank Application. \nTo login to an existing account, enter 1\n"
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
								+ "\n6. View pending accounts \n7. Cancel an account\n8. Lookup Customer Information\n9.Exit");
				try {
					num = scan.nextInt();
					if (num > 0 && num < 9) {
						done = true;
					} else if (num == 9) {
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
								+ "\n3. Make a deposit\n4. Transfer funds between accounts\n5. Create a new account\n6. Exit");
				try {
					num = scan.nextInt();
					if (num > 0 && num < 6) {
						done = true;
					} else if (num == 6) {
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

		LOGGER.info("User opened the view accounts menu");

		if (currentUser.getAccounts().size() == 0) {
			System.out.println("You currently have no open accounts.");
			LOGGER.debug("User had no open accounts");
		} else {
			for (int i : currentUser.getAccounts()) {
				System.out.println(ledger.getAccount(i));
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
							|| (!isEmployee && acc.getUsers().indexOf(currentUser.getUsername()) > -1))) {
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
							|| (!isEmployee && acc.getUsers().indexOf(currentUser.getUsername()) > -1))) {
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
						System.out.println("Transaction successful");
						done = true;
					} else {
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
			}
		}
	}

	public void pendingAccMenu() {
		boolean done = false;
		int num = 0;
		String temp = null;
		scan.nextLine();

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
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
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
					break;
				} else if (temp.toLowerCase().equals("reject") || temp.toLowerCase().equals("n")) {
					ledger.cancelAcc(num);
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
		scan.nextLine();

		while (!done) {
			while (!done) {
				System.out.println("Please enter the id of the account you wish to cancel");
				try {
					num = scan.nextInt();
					if (ledger.getAccount(num) != null) {
						done = true;
					} else {
						System.out.println("Account not found");
						scan.nextLine();
					}
				} catch (InputMismatchException e) {
					temp = scan.nextLine();
					if (temp.toLowerCase().equals("back")) {
						return;
					}
				}
			}
			done = false;
			while (!done) {
				System.out.println("Are you sure you wish to cancel \"" + ledger.getAccount(num) + "\"? Y or N");
				temp = scan.nextLine();
				if (temp.toLowerCase().equals("y")) {
					ledger.cancelAcc(num);
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

	public void exit() {
		ledger.closeLedger();
	}

	public void testLedgerSetup() {
		ArrayList<String> acc1 = new ArrayList<String>();
		ArrayList<String> acc2 = new ArrayList<String>();
		ArrayList<String> acc3 = new ArrayList<String>();
		acc1.add("admin");
		acc2.add("user");
		acc3.add("admin");
		acc3.add("user");

		ledger.addUser(true, "admin", "password");
		ledger.addUser(false, "user", "abc123");
		ledger.addUser(false, "customer", "secure456");

		int num1 = ledger.createAcc(acc1);
		ledger.approveAcc(num1);
		ledger.deposit(num1, 100);
		ledger.createAcc(acc2);
		int num3 = ledger.createAcc(acc3);
		ledger.approveAcc(num3);
		ledger.deposit(num3, 50);
	}
}
