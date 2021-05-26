package com.revature.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.revature.beans.Account;
import com.revature.utils.ConnectionUtils;

public class AccountDAOImpl implements AccountDAO{

	public int addAccount(Account acc) {
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select add_account(?, ?)";
			PreparedStatement statement = conn.prepareCall(sql);
			statement.setInt(1, acc.getAmount());
			statement.setBoolean(2, acc.isPending());
			ResultSet result = statement.executeQuery(); 
			result.next();
			int id = result.getInt(1);
			
			sql = "insert into users_accounts(user_id,bank_account_id)"
					+"values(?,?)";
			statement = conn.prepareStatement(sql);
			statement.setInt(2, id);
			for(Integer i : acc.getUsers()) {
				statement.setInt(1, i);
				statement.execute();
			}
			return id;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return -1;
	}

	public boolean removeAccount(int acc) {
		try(Connection conn = ConnectionUtils.getConnection()) {	
			String sql = "delete from users_accounts where bank_account_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, acc);
			statement.execute();
			
			sql = "delete from accounts where bank_account_id = ?";
			statement = conn.prepareStatement(sql);
			statement.setInt(1, acc);
			statement.execute();

			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	public List<Account> getListOfAccounts() {
		try(Connection conn = ConnectionUtils.getConnection()) {
			ArrayList<Account> accs = new ArrayList<Account>();
			ArrayList<Integer> accUsers = new ArrayList<Integer>();
 			ResultSet result2 = null;
			
			String sql = "select * from accounts";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			String sql2 = "select user_id from users_accounts where bank_account_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql2);
			
			
			while(result.next()) {
				Account acc = new Account(
						result.getInt("bank_account_id"),
						result.getInt("amount"),
						result.getBoolean("pending")
						);
				pStatement.setInt(1, acc.getAID());
				result2 = pStatement.executeQuery();
				while(result2.next()) {
					accUsers.add(result2.getInt("user_id"));
				}
				acc.setUsers(accUsers);
				accs.add(acc);
				accUsers = new ArrayList<Integer>();
			}
			
			return accs;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Account getAccountByID(int id) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtils.getConnection()) {
			Account acc = new Account();
			acc.setAID(id);
			ArrayList<Integer> accUsers = new ArrayList<Integer>();
 			ResultSet result2 = null;
			
			String sql = "select * from accounts where bank_account_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, id);
			ResultSet result = pStatement.executeQuery();
			if(result.next()) {
				acc.setAmount(result.getInt("amount"));
				acc.setPending(result.getBoolean("pending"));
			}
			
			String sql2 = "select user_id from users_accounts where bank_account_id = ?";
			PreparedStatement pStatement2 = conn.prepareStatement(sql2);
			pStatement2.setInt(1, id);
			result2 = pStatement2.executeQuery();
			while(result2.next()) {
				accUsers.add(result2.getInt("user_id"));
			}
			acc.setUsers(accUsers);

			
			return acc;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public boolean withdraw(int acc, int amount) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select withdraw(?, ?)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, acc);
			pStatement.setInt(2, amount);
			pStatement.execute();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean deposit(int acc, int amount) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select deposit(?, ?)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, acc);
			pStatement.setInt(2, amount);
			pStatement.execute();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	public boolean transfer(int accFrom, int accTo, int amount) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select transfer(?, ?, ?)";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, accFrom);
			pStatement.setInt(2, accTo);
			pStatement.setInt(3, amount);
			pStatement.execute();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}

	public boolean approveAcc(int id) {
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "update accounts set pending = false where bank_account_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, id);
			pStatement.execute();
			
			return true;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}
