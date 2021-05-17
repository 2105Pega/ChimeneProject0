package com.revature.test.engine;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import com.revature.engine.LedgerEngine;
import com.revature.user.*;

class EngineTests {
	private static LedgerEngine ledger = new LedgerEngine();
	
	@Test
	void loadTest() {
		assertEquals(0,ledger.initializeLedger());
	}
	
	@Test
	void writeTest() {
		assertEquals(0,ledger.closeLedger());
	}
	
	@Test
	void addUserTest() {
		int temp = ledger.getNumUsers();
		ledger.addUser(false, "user", "password");
		assertNotNull(ledger.getUser("user"));
	}
	
	@Test
	void createAccTest() {
		ArrayList<String> names = new ArrayList<String>();
		ledger.addUser(false, "temp", "password");
		ledger.addUser(false, "temp2", "password");
		ledger.addUser(false, "temp3", "password");
		names.add("temp");
		names.add("temp2");
		names.add("temp3");
		int id = ledger.createAcc(names);
		assertNotNull(ledger.getAccount(id));
		assertEquals(1, ledger.getUser("temp").getAccounts().size());
	}
	
	@Test 
	void cancelAccTest() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		ledger.addUser(false, "temp", "password");
		int id = ledger.createAcc(names);
		ledger.cancelAcc(id);
		assertNull(ledger.getAccount(id));
	}
	
	@Test
	void depositTest() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		int id = ledger.createAcc(names);
		ledger.deposit(id, 100);
		assertEquals(100, ledger.getAccount(id).getAmount());
	}
	
	@Test
	void withdrawTest() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		int id = ledger.createAcc(names);
		ledger.deposit(id, 100);
		ledger.withdraw(id, 50);
		assertEquals(50, ledger.getAccount(id).getAmount());
	}
	
	@Test
	void transferTest() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		ledger.addUser(false, "temp", "password");
		int id = ledger.createAcc(names);
		int id2 = ledger.createAcc(names);
		ledger.deposit(id, 100);
		ledger.transfer(id, id2, 50);
		assertEquals(50, ledger.getAccount(id).getAmount());
		assertEquals(50, ledger.getAccount(id2).getAmount());
	}
	
	@Test
	void approveTest() {
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		int id = ledger.createAcc(names);
		ledger.approveAcc(id);
		assertFalse(ledger.getAccount(id).isPending());
	}
	
	@Test
	void pendingTest() {
		ledger.addUser(false, "temp", "password");
		ledger.addUser(false, "temp2", "password");
		ArrayList<String> names = new ArrayList<String>();
		names.add("temp");
		int id = ledger.createAcc(names);
		ArrayList<String> names2 = new ArrayList<String>();
		names2.add("temp2");
		int id2 = ledger.createAcc(names2);
		ledger.approveAcc(id);
		assertFalse(ledger.hasPending("temp"));
		assertTrue(ledger.hasPending("temp2"));
	}
}

