package com.revature.engine;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import com.revature.user.*;

public class LedgerEngine {
	private HashMap<String, User> userList;
	private HashMap<Integer, Account> accountList;
	private ArrayList<Account> pendingAcc;
	private static final int ID_BASE = 11111;

	public LedgerEngine() {
		userList = new HashMap<String, User>();
		accountList = new HashMap<Integer, Account>();
		pendingAcc = new ArrayList<Account>();
	}

	public int initializeLedger() {
		FileInputStream fileIn = null;
		ObjectInputStream input = null;
		User user;
		Account acc;

		try {
			fileIn = new FileInputStream(System.getProperty("user.dir") + "/Ledger.txt");
			input = new ObjectInputStream(fileIn);
			try {
				while ((user = (User) input.readObject()) != null) {
					userList.put(user.getUsername().toLowerCase(), user);
				}
			} catch (java.io.EOFException e) {
				// We hit the end of file while reading, so java is going to throw an exception,
				// but program is still safe to run
			}
			fileIn.close();
			input.close();
			fileIn = new FileInputStream(System.getProperty("user.dir") + "/Accounts.txt");
			input = new ObjectInputStream(fileIn);
			try {
				while ((acc = (Account) input.readObject()) != null) {
					accountList.put(acc.getAID(), acc);
					if (acc.isPending()) {
						pendingAcc.add(acc);
					}
				}
			} catch (java.io.EOFException e) {
				// We hit the end of file while reading, so java is going to throw an exception,
				// but program is still safe to run
			}
		} catch (FileNotFoundException e) {
			System.out.println("Ledger not found: " + e);
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			System.out.println("IO exception: " + e);
			e.printStackTrace();
			return -1;
		} catch (ClassNotFoundException e) {
			System.out.println("Object not read properly: " + e);
			e.printStackTrace();
		} finally {
			if (fileIn != null) {
				try {
					fileIn.close();
				} catch (IOException e) {
					System.out.println("Failed to close FileInputStream: " + e);
					e.printStackTrace();
					return -1;
				}
			}
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					System.out.println("Failed to close ObjectInputStream: " + e);
					e.printStackTrace();
					return -1;
				}
			}
		}

		return 0;
	}

	public int closeLedger() {
		FileOutputStream fileOut = null;
		ObjectOutputStream output = null;

		try {
			fileOut = new FileOutputStream(System.getProperty("user.dir") + "/Ledger.txt");
			output = new ObjectOutputStream(fileOut);
			for (User user : userList.values()) {
				output.writeObject(user);
			}
			fileOut.close();
			output.close();
			fileOut = new FileOutputStream(System.getProperty("user.dir") + "/Accounts.txt");
			output = new ObjectOutputStream(fileOut);
			for (Account acc : accountList.values()) {
				output.writeObject(acc);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Ledger not found: " + e);
			e.printStackTrace();
			return -1;
		} catch (IOException e) {
			System.out.println("IO exception: " + e);
			e.printStackTrace();
			return -1;
		} finally {
			if (fileOut != null) {
				try {
					fileOut.close();
				} catch (IOException e) {
					System.out.println("Failed to close FileInputStream: " + e);
					e.printStackTrace();
					return -1;
				}
			}
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					System.out.println("Failed to close ObjectInputStream: " + e);
					e.printStackTrace();
					return -1;
				}
			}
		}

		return 0;
	}

	public User getUser(String name) {
		return userList.get(name);
	}

	public Account getAccount(int id) {
		return accountList.get(id);
	}

	public ArrayList<Account> getPendingAcc() {
		return pendingAcc;
	}

	public int getNumUsers() {
		return userList.size();
	}

	public int getNumAccounts() {
		return accountList.size();
	}

	public void addUser(boolean emp, String username, String password) {
		userList.put(username, new User(emp, username, password, new ArrayList<Integer>()));
	}

	public void withdraw(int account, int amount) {
		this.getAccount(account).withdraw(amount);
	}

	public void deposit(int account, int amount) {
		this.getAccount(account).deposit(amount);
	}

	public void transfer(int accFrom, int accTo, int amount) {
		this.withdraw(accFrom, amount);
		this.deposit(accTo, amount);
	}

	public void cancelAcc(int id) {
		ArrayList<String> users = accountList.get(id).getUsers();
		for (String str : users) {
			userList.get(str).removeAccount(id);
		}
		accountList.remove(id);
	}

	public void approveAcc(int id) {
		accountList.get(id).setPending(false);
		pendingAcc.remove(pendingAcc.indexOf(accountList.get(id)));
	}

	public boolean hasPending(String name) {
		ArrayList<Integer> acc = userList.get(name).getAccounts();
		for (Integer i : acc) {
			if (accountList.get(i).isPending()) {
				return true;
			}
		}
		return false;
	}

	public int createAcc(ArrayList<String> names) {
		int id = accountList.size() > 0 ? Collections.max(accountList.keySet()) + 1 : ID_BASE;
		
		Account acc = new Account(names, 0, true, id);
		accountList.put(acc.getAID(), acc);
		pendingAcc.add(acc);
		for (String str : names) {
			userList.get(str).addAccount(acc.getAID());
		}
		return acc.getAID();
	}
}
