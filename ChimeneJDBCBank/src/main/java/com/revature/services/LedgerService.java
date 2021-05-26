package com.revature.services;

import java.util.ArrayList;
import java.util.HashMap;

import com.revature.beans.Account;
import com.revature.beans.Transaction;
import com.revature.beans.User;
import com.revature.dao.*;

public class LedgerService {
	private HashMap<String, User> userList;
	private HashMap<Integer, Account> accountList;
	private ArrayList<Account> pendingAcc;
	private static UserDAOImpl uDao = new UserDAOImpl();
	private static AccountDAOImpl aDao = new AccountDAOImpl();
	private static TransactionDAOImpl tDao = new TransactionDAOImpl();
	

	public LedgerService() {
		userList = new HashMap<String, User>();
		accountList = new HashMap<Integer, Account>();
		pendingAcc = new ArrayList<Account>();
	}

	public int initializeLedger() {
		ArrayList<User> uList = (ArrayList<User>) uDao.getListOfUsers();
		if(uList == null) {
			return -1;
		}
		ArrayList<Account> aList = (ArrayList<Account>) aDao.getListOfAccounts();
		if(aList == null) {
			return -1;
		}
		
		for(User u : uList) {
			userList.put(u.getUsername(), u);
		}
		
		for(Account a : aList) {
			accountList.put(a.getAID(), a);
			if(a.isPending()) {
				pendingAcc.add(a);
			}
		}
		
		return 0;
	}

	public User getUser(String name) {
		return userList.get(name);
	}
	
	public String getName(int id) {
		return uDao.getNameByID(id);
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
		User u = new User(emp, username, password, new ArrayList<Integer>());
		userList.put(username, u);
		uDao.addUser(u);
	}

	public void withdraw(int account, int amount) {
		this.getAccount(account).setAmount(this.getAccount(account).getAmount() - amount);
		aDao.withdraw(account, amount);
	}

	public void deposit(int account, int amount) {
		this.getAccount(account).setAmount(this.getAccount(account).getAmount() + amount);
		aDao.deposit(account, amount);
	}

	public void transfer(int accFrom, int accTo, int amount) {
		this.withdraw(accFrom, amount);
		this.deposit(accTo, amount);
		aDao.transfer(accFrom, accTo, amount);
	}

	public void cancelAcc(int id) {
		ArrayList<Integer> users = accountList.get(id).getUsers();
		for (Integer i : users) {
			userList.get(uDao.getNameByID(i)).removeAccount(id);
		}
		accountList.remove(id);
		aDao.removeAccount(id);
	}

	public void approveAcc(int id) {
		accountList.get(id).setPending(false);
		pendingAcc.remove(pendingAcc.indexOf(accountList.get(id)));
		aDao.approveAcc(id);
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
		ArrayList<Integer> ids = new ArrayList<Integer>();
		for(String str : names) {
			ids.add(uDao.getUserByName(str).getUid());
		}
		
		Account acc = new Account(ids, 0, true, 1);
		acc.setAID(aDao.addAccount(acc));
		accountList.put(acc.getAID(), acc);
		pendingAcc.add(acc);
		for (Integer str : ids) {
			userList.get(uDao.getNameByID(str)).addAccount(acc.getAID());
		}
		return acc.getAID();
	}
	
	public void createUser(User u) {
		userList.put(u.getUsername(), u);
		uDao.addUser(u);
	}
	
	public void removeUser(String name) {
		uDao.removeUser(userList.get(name));
		userList.remove(name);
	}
	
	public void updateUser(User u, String name) {
		uDao.updateUser(userList.get(name), u);
		userList.get(name).setUsername(u.getUsername());
		userList.get(name).setPassword(u.getPassword());
	}
	
	public ArrayList<Transaction> getTransactionList(int id) {
		return (ArrayList <Transaction>) tDao.getTransactionsByAccount(id);
	}
}
