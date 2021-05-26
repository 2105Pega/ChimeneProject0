package com.revature.test.dao;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.revature.beans.Account;
import com.revature.beans.User;
import com.revature.dao.AccountDAOImpl;
import com.revature.dao.TransactionDAOImpl;
import com.revature.dao.UserDAOImpl;

public class DAOTests {
	private static AccountDAOImpl  aDAO = new AccountDAOImpl();
	private static UserDAOImpl  uDAO = new UserDAOImpl();
	private static TransactionDAOImpl  tDAO = new TransactionDAOImpl();
	
	//Put some dummy data in the db before hand for test purposes

	@Test
	public void addAccountTest() {
		Account acc = new Account();
		acc.setAmount(70);
		
		ArrayList<Integer> users = new ArrayList<Integer>();
		users.add(1);
		users.add(3);
		acc.setUsers(users);
		
		assertNotEquals(-1, aDAO.addAccount(acc));
	}
	
	@Test
	public void removeAccountTest() {
		assertTrue(aDAO.removeAccount(7));
	}

	@Test
	public void getListOfAccountsTest() {
		assertNotNull(aDAO.getListOfAccounts());
	}
	
	@Test
	public void getAccountByIDTest() {
		assertNotNull(aDAO.getAccountByID(1));
	}
	
	@Test
	public void withdrawTest() {
		assertTrue(aDAO.withdraw(5, 10));
	}
	
	@Test
	public void depositTest() {
		assertTrue(aDAO.deposit(5, 10));
	}
	
	@Test
	public void transferTest() {
		assertTrue(aDAO.transfer(5, 9, 10));
	}
	
	@Test
	public void approveAccTest() {
		assertTrue(aDAO.approveAcc(5));
	}
	
	@Test
	public void addUserTest() {
		ArrayList<Integer> accs = new ArrayList<Integer>();
		accs.add(1);
		User u = new User(false, "test", "password", accs);
		
		assertTrue(uDAO.addUser(u));
	}
	
	@Test
	public void removeUserTest() {
		User u = new User();
		u.setUid(5);
		
		assertTrue(uDAO.removeUser(u));
	}
	
	@Test
	public void getListOfUsersTest() {
		assertNotNull(uDAO.getListOfUsers());
	}
	
	@Test
	public void getUserByNameTest() {
		assertNotNull(uDAO.getUserByName("user1"));
	}
	
	@Test
	public void updateUserTest() {
		User oldUser = new User();
		oldUser.setUsername("user2");
		
		User newUser = new User();
		newUser.setUsername("newUsername");
		newUser.setPassword("newPassword");
		
		assertTrue(uDAO.updateUser(oldUser, newUser));
	}
	
	@Test
	public void getTransactionsByAccountTest() {
		assertNotNull(tDAO.getTransactionsByAccount(5));
	}
}
