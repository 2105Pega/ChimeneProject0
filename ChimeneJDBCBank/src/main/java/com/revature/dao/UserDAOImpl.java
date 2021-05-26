package com.revature.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.revature.beans.User;
import com.revature.utils.ConnectionUtils;

public class UserDAOImpl implements UserDAO{

	@Override
	public boolean addUser(User u) {
		// TODO Auto-generated method stub
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select add_user(?,?,?)";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setBoolean(1, u.isEmployee());
			statement.setString(2, u.getUsername());
			statement.setString(3, u.getPassword());
			ResultSet result = statement.executeQuery(); 
			result.next();
			int id = result.getInt(1);
			
			sql = "insert into users_accounts(user_id,bank_account_id)"
					+"values(?,?)";
			statement = conn.prepareStatement(sql);
			statement.setInt(1, id);
			for(Integer i : u.getAccounts()) {
				statement.setInt(2, i);
				statement.execute();
			}
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean removeUser(User u) {
		try(Connection conn = ConnectionUtils.getConnection()) {		
			String sql = "delete from users_accounts where user_id = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, u.getUid());
			statement.execute();
			
			sql = "delete from users where user_id = ?";
			statement = conn.prepareStatement(sql);
			statement.setInt(1, u.getUid());
			statement.execute();
			
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return false;
	}

	@Override
	public List<User> getListOfUsers() {
		try(Connection conn = ConnectionUtils.getConnection()) {
			ArrayList<User> users = new ArrayList<User>();
			ArrayList<Integer> accs = new ArrayList<Integer>();
 			ResultSet result2 = null;
			
			String sql = "select * from users";
			Statement statement = conn.createStatement();
			ResultSet result = statement.executeQuery(sql);
			String sql2 = "select bank_account_id from users_accounts where user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql2);
			
			
			while(result.next()) {
				User u = new User(
							result.getBoolean("is_employee"),
							result.getString("username"),
							result.getString("u_password"),
							result.getInt("user_id")
						);
				pStatement.setInt(1, u.getUid());
				result2 = pStatement.executeQuery();
				while(result2.next()) {
					accs.add(result2.getInt("bank_account_id"));
				}
				u.setAccounts(accs);
				users.add(u);
				accs = new ArrayList<Integer>();
			}
			
			return users;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public User getUserByName(String name) {
		try(Connection conn = ConnectionUtils.getConnection()) {
			User u = new User();
			u.setUsername(name);
			ArrayList<Integer> accs = new ArrayList<Integer>();
 			ResultSet result2 = null;
			
			String sql = "select * from users where username = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setString(1, name);
			ResultSet result = pStatement.executeQuery();
			result.next();
			u.setUid(result.getInt("user_id"));
			u.setPassword(result.getString("u_password"));
			u.setEmployee(result.getBoolean("is_employee"));
			
			String sql2 = "select bank_account_id from users_accounts where user_id = ?";
			PreparedStatement pStatement2 = conn.prepareStatement(sql2);
			pStatement2.setInt(1, u.getUid());
			result2 = pStatement2.executeQuery();
			while(result2.next()) {
				accs.add(result2.getInt("bank_account_id"));
			}
			u.setAccounts(accs);
			
			return u;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public boolean updateUser(User oldUser, User newUser) { 
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "update users set username = ?, u_password = ? where username = ?";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, newUser.getUsername());
			statement.setString(2, newUser.getPassword());
			statement.setString(3, oldUser.getUsername());
			statement.execute();
			return true;
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return false;
	}

	@Override
	public String getNameByID(int id) {
		try(Connection conn = ConnectionUtils.getConnection()) {
			String sql = "select * from users where user_id = ?";
			PreparedStatement pStatement = conn.prepareStatement(sql);
			pStatement.setInt(1, id);
			ResultSet result = pStatement.executeQuery();
			result.next();
			
			return result.getString("username");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
