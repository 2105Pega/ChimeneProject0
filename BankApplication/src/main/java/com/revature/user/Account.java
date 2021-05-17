package com.revature.user;

import java.util.ArrayList;

public class Account implements java.io.Serializable{
	private int aid;
	private int amount;
	private ArrayList<String> users;
	private boolean isPending;
	
	public Account() {
		aid = 0;
		users = new ArrayList<String>();
		isPending = false;
	}
	
	public Account(ArrayList<String> names , int val, boolean pending, int id) {
		users = new ArrayList<String>();
		aid = id;
		for(String s:names) {
			users.add(s);
		}
		amount = val;
		isPending = pending;
	}
	

	public int getAID() {
		return aid;
	}

	public void setAID(int aid) {
		this.aid = aid;
	}

	public boolean isPending() {
		return isPending;
	}

	public void setPending(boolean isPending) {
		this.isPending = isPending;
	}

	public ArrayList<String> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<String> users) {
		this.users = users;
	}
	
	public void addUser(String s) {
		users.add(s);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public void withdraw(int amount) {
		this.amount -= amount;
	}
	
	public void deposit(int amount) {
		this.amount += amount;
	}
	
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("(" + aid  +")");
		if(isPending) {
			str.append("Pending ");
		}
		if(users.size() == 1) {
			str.append("Individual Account ");
		}
		else if(users.size() > 1) {
			str.append("Joint Account held by ");
			for(int i=0; i<users.size()-1; i++) {
				str.append(users.get(i) + ", ");
			}
			str.append("and " + users.get(users.size()-1) + " ");
		}
		str.append("that contains $" + amount);
		
		return str.toString();
	}
}
