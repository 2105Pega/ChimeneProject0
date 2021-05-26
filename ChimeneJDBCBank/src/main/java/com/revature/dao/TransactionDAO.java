package com.revature.dao;

import java.util.List;

import com.revature.beans.Transaction;

public interface TransactionDAO {
	public boolean addTransaction(Transaction t);
	public List<Transaction> getTransactionsByAccount(int id);
}
