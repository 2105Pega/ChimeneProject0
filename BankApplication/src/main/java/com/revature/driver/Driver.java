package com.revature.driver;

import com.revature.menu.Menu;

public class Driver {
	
	public static void main(String[] args) {
		int choice;
		boolean done = false;
		Menu menu = new Menu();
		
		menu.testLedgerSetup();
		menu.exit();
		
		while(!done) {
			choice = menu.entryPoint();
			System.out.println("You may return to the previous menu by entering \"back\"");
			if (choice == 1) {
				done = menu.login();
			} else {
				done = menu.register();
			}
		}
		done = false;
		
		while(!done) {
			switch(menu.mainMenu()) {
				case -1: //Exit condition
					menu.exit();
					done = true;
					break;
				case 1: //View the users accounts
					menu.accountsMenu();
					break;
				case 2: //Make a withdrawal
					menu.transactionMenu(true);
					break;
				case 3: //Make a deposit
					menu.transactionMenu(false);
					break;
				case 4: //Make a transfer
					menu.transferMenu();
					break;
				case 5: //Create a new account
					menu.openAccount();
					break;
				case 6: //View pending accounts
					menu.pendingAccMenu();
					break;
				case 7: //Cancel account
					menu.cancelAccMenu();
					break;
				case 8: //Customer lookup
					menu.customerLookup();
					break;
				default:
					break;
			}
		}
		
	}
}
