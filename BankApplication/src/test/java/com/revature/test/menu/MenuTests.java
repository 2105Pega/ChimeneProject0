package com.revature.test.menu;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revature.engine.LedgerEngine;

public class MenuTests {
	private static TestMenu menu = new TestMenu();
	private static LedgerEngine ledger = new LedgerEngine();
	
	@BeforeAll
	static void openLedger() {
		ledger.initializeLedger();
		menu.testLedgerSetup();
	}
	
	@Test
	void entryPointTest() {
		assertEquals(0, menu.entryPoint(1));
		assertEquals(1, menu.entryPoint(9));
	}
	
	@Test 
	void loginTest() {
		assertTrue(menu.login("admin", "password"));
		assertFalse(menu.login("",""));
	}
	
	@Test
	void registerTest() {
		assertTrue(menu.register("n", "bob", "theBuilder"));
		assertFalse(menu.register("n", "", ""));
		assertFalse(menu.register("y","admin", "password"));
		assertFalse(menu.register("wuidhw", "", ""));
	}
	
	@Test
	void mainMenuTest() {
		assertEquals(0, menu.mainMenu(2));
		assertEquals(-1, menu.mainMenu(-1));
	}
	
	@Test
	void createAccTest() {
		assertEquals(1, menu.openAccount("back", ""));
		assertEquals(-1, menu.openAccount("y", ""));
		assertEquals(1, menu.openAccount("n", "back"));
		assertEquals(-1, menu.openAccount("n", "awiofadhfjaf"));
		assertEquals(0, menu.openAccount("n", "admin,customer"));
	}
	
	@Test
	void transactionTest() {
		assertEquals(-1, menu.transactionMenu(true, 0, 0));
		assertEquals(0, menu.transactionMenu(true, 11111, 10));
		assertEquals(-1, menu.transactionMenu(true, 11111, -10));
		assertEquals(-1, menu.transactionMenu(true, 11111, 11111));
		assertEquals(-1, menu.transactionMenu(false, 11111, -10));
		assertEquals(0, menu.transactionMenu(false, 11111, 50));
	}
	
	@Test
	void transferTest() {
		assertEquals(-1, menu.transferMenu(0, 0, 0));
		assertEquals(-1, menu.transferMenu(11111, 0, 0));
		assertEquals(-1, menu.transferMenu(11111, 11112, -10));
		//assertEquals(0, menu.transferMenu(11111, 11112, 10));
	}
	
	@Test
	void lookupTest() {
		assertEquals(-1, menu.customerLookup(""));
		assertEquals(0, menu.customerLookup("admin"));
	}
	
	@Test
	void pendingAccTest() {
		assertEquals(-1, menu.pendingAccMenu(0, ""));
		assertEquals(0, menu.pendingAccMenu(11112, "y"));
		assertEquals(1, menu.pendingAccMenu(11112, "n"));
		assertEquals(-1, menu.pendingAccMenu(11112, "wdhuawd"));
	}
	
	@Test 
	void cancelAccTest() {
		assertEquals(-1, menu.cancelAccMenu(0, ""));
		assertEquals(0, menu.cancelAccMenu(11111, "y"));
		assertEquals(1, menu.cancelAccMenu(11111, "n"));
		assertEquals(-1, menu.cancelAccMenu(11111, "wdhuawd"));
	}
}
