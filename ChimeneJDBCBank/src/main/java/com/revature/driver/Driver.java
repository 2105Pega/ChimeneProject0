package com.revature.driver;

import com.revature.services.MenuService;

public class Driver {
	public static void main(String[] args) {
		int choice;
		boolean done = false;
		boolean logOut = false;
		MenuService menu = new MenuService();
		menu.initializeMenu();
		
		while (!done) {
			while (!done) {
				choice = menu.entryPoint();
				System.out.println("You may return to the previous menu by entering \"back\"");
				if (choice == 1) {
					done = menu.login();
				} else {
					done = menu.register();
				}
			}
			done = false;

			while (!done) {
				switch (menu.mainMenu()) {
				case -1: // Exit condition
					done = true;
					break;
				case 0: // Log out
					done = true;
					logOut = true;
					break;
				case 1: // View the users accounts
					menu.accountsMenu();
					break;
				case 2: // Make a withdrawal
					menu.transactionMenu(true);
					break;
				case 3: // Make a deposit
					menu.transactionMenu(false);
					break;
				case 4: // Make a transfer
					menu.transferMenu();
					break;
				case 5: // Create a new account
					menu.openAccount();
					break;
				case 6: // View pending accounts
					menu.pendingAccMenu();
					break;
				case 7: // Cancel account
					menu.cancelAccMenu();
					break;
				case 8: // Customer lookup
					menu.customerLookup();
					break;
				case 9: // Create user
					menu.register();
					break;
				case 10: //Update user
					menu.updateUser();
					break;
				case 11: //Remove user
					menu.removeUser();
					break;
				case 12: //Close an empty account
					menu.cancelEmptyAccount();
					break;
				case 13: //Transaction history
					menu.transactionList();
					break;
				default:
					break;
				}
			}
			if (logOut) {
				done = false;
				logOut = false;
			}
		}
		
		System.out.println("Goodbye!");
	}
}
