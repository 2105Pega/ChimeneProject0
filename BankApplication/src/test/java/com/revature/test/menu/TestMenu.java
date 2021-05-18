package com.revature.test.menu;

import java.util.ArrayList;
import java.util.InputMismatchException;

import com.revature.engine.LedgerEngine;
import com.revature.user.Account;
import com.revature.user.User;

/*This is a class designed to help test some of the menu methods
by replacing the prompting and scanning of user errors with parameters
and making the methods return booleans or ints*/
public class TestMenu {
	private static LedgerEngine ledger;
	private static User currentUser;
	private static boolean isEmployee;

	public TestMenu() {
		if (ledger == null) {
			ledger = new LedgerEngine();
			currentUser = null;
			isEmployee = false;
		}
	}

	public int entryPoint(int choice) {
		ledger.initializeLedger();
		try {
			// choice = scan.nextInt();
			if (choice == 1 || choice == 2) {
				return 0;
			} else {
				return 1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
	}

	public boolean login(String username, String password) {
		User user = null;
		if (username.toLowerCase().equals("back")) {
			return false;
		}
		user = ledger.getUser(username.toLowerCase());
		if (user == null) {
			return false;
		}
		if (password.equals(user.getPassword())) {
			currentUser = user;
			isEmployee = user.isEmployee();
			return true;
		} else {
			return false;
		}
	}

	public boolean register(String answer, String username, String password) {
		boolean emp = false;

		if (answer.toLowerCase().equals("y")) {
			emp = true;
		} else if (answer.toLowerCase().equals("n")) {
			emp = false;
		} else {
			return false;
		}

		if (username.toLowerCase().equals("back")) {
			return false;
		} else if (username.length() > 0 && ledger.getUser(username) == null) {
		} else {
			return false;
		}

		// password = scan.nextLine();
		if (password.length() > 0) {
			return true;
		}

		ledger.addUser(emp, username, password);
		currentUser = ledger.getUser(username);
		isEmployee = emp;
		return true;
	}

	public int mainMenu(int num) {

		try {
			if (num > 0 && num < 9) {
				return 0;
			} else if (num == 9) {
				return -1;
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
	}

	public int openAccount(String answer, String answer2) {
		boolean done = false;
		ArrayList<String> names = new ArrayList<>();
		int index;
		String name = null;
		if (answer.toLowerCase().equals("back")) {
			return 1;
		} else if (answer.toLowerCase().equals("y")) {
			ledger.createAcc(names);
			return -1;
		} else if (answer.toLowerCase().equals("n")) {
			while (!done) {
				// answer = scan.nextLine();
				if (answer2.toLowerCase().equals("back")) {
					return 1;
				}
				index = 0;
				while (answer2.indexOf(",") > 0) {
					name = answer2.substring(index, answer2.indexOf(","));
					if (ledger.getUser(name) != null) {
						names.add(name);
						index = answer2.indexOf(",") + 1;
						answer2 = answer2.substring(index);
						index = 0;
					} else {
						return -1;
					}
				}
				if (ledger.getUser(name) == null) {
				} else if (ledger.getUser(answer) != null) {
					names.add(answer);
					ledger.createAcc(names);
					return 0;
				} else {
					return 1;
				}
			}
		}

		return 0;
	}

	public int transactionMenu(boolean isWithdrawal, int num, int amount) {
		Account acc = null;

		try {
			// num = scan.nextInt();
			acc = ledger.getAccount(num);
			if (acc != null && !acc.isPending()
					&& (isEmployee || (!isEmployee && acc.getUsers().indexOf(currentUser.getUsername()) > -1))) {
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
		try {
			// amount = scan.nextInt();
			if (!isWithdrawal && amount > 0) {
				ledger.deposit(num, amount);
				return 0;
			} else if (isWithdrawal && ledger.getAccount(num).getAmount() >= amount && amount > 0) {
				ledger.withdraw(num, amount);
				return 0;
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
	}

	public int transferMenu(int accF, int accT, int amount) {
		Account acc = null;

		try {
			acc = ledger.getAccount(accF);
			if (acc != null && !acc.isPending()) {
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
		try {
			// accT = scan.nextInt();
			if (accT != accF && ledger.getAccount(accT) != null && !ledger.getAccount(accT).isPending()) {
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			// temp = scan.nextLine();
			return -1;
		}
		try {
			// amount = scan.nextInt();
			if (amount > 0 && acc.getAmount() > amount) {
				ledger.transfer(accF, accT, amount);
				return 0;
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}

	}

	public int customerLookup(String answer) {
		// answer = scan.nextLine();
		User user = null;

		if (answer.toLowerCase().equals("back")) {
			return -1;
		} else if ((user = ledger.getUser(answer)) != null) {
			return 0;
		} else {
			return -1;
		}
	}

	public int pendingAccMenu(int num, String temp) {
		try {
			// num = scan.nextInt();
			if (ledger.getAccount(num) != null && ledger.getAccount(num).isPending()) {

			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}

		// temp = scan.nextLine();
		if (temp.toLowerCase().equals("back")) {
			return -1;
		} else if (temp.toLowerCase().equals("approve") || temp.toLowerCase().equals("y")) {
			return 0;
		} else if (temp.toLowerCase().equals("reject") || temp.toLowerCase().equals("n")) {
			return 1;
		}
		return -1;
	}

	public int cancelAccMenu(int num, String temp) {
		try {
			// num = scan.nextInt();
			if (ledger.getAccount(num) != null) {
			} else {
				return -1;
			}
		} catch (InputMismatchException e) {
			return -1;
		}
		if (temp.toLowerCase().equals("y")) {
			return 0;
		} else if (temp.toLowerCase().equals("n")) {
			return 1;
		} else if (temp.toLowerCase().equals("back")) {
			return -1;
		}

		return -1;

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
