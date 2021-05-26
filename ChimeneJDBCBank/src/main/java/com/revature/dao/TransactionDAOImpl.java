package com.revature.dao;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.revature.beans.Transaction;
import com.revature.utils.ConnectionUtils;

public class TransactionDAOImpl implements TransactionDAO{
	public boolean addTransaction(Transaction t) {
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "insert into transactions(acc_from, acc_to, amount, type_id) "
					+ "values(?,?,?,?)";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, t.getAccFrom());
			statement.setInt(2, t.getAccTo());
			statement.setInt(3, t.getAmount());
			statement.setInt(4, t.getType());
			
			statement.execute();
			
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}
	public List<Transaction> getTransactionsByAccount(int id) {
		ArrayList<Transaction> trans = new ArrayList<Transaction>();
		
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select * from transactions where acc_from = ? or acc_to = ?";
			
			PreparedStatement statement = conn.prepareStatement(sql);
			
			statement.setInt(1, id);
			statement.setInt(2, id);
			
			ResultSet result = statement.executeQuery();
			
			while(result.next()) {
				Transaction t = new Transaction(
						result.getInt("acc_from"),
						result.getInt("acc_to"),
						result.getInt("amount"),
						result.getInt("type_id"),
						result.getTimestamp("ts"));
				trans.add(t);
			}
			
			return trans;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
}
