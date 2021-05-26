package com.revature.dao;

import java.util.List;

import com.revature.beans.Account;

public interface AccountDAO {
	public int addAccount(Account acc);
	public boolean removeAccount(int acc);
	public List<Account> getListOfAccounts();
	public Account getAccountByID(int id);
	public boolean withdraw(int acc, int amount);
	public boolean deposit(int acc, int amount);
	public boolean transfer(int accFrom, int accTo, int amount);
	public boolean approveAcc(int id);
}
