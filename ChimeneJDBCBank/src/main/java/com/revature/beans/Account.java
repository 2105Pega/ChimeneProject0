package com.revature.beans;

import java.util.ArrayList;

public class Account {
	private int aid;
	private int amount;
	private ArrayList<Integer> users;
	private boolean isPending;
	
	public Account() {
		aid = 0;
		amount = 0;
		users = new ArrayList<Integer>();
		isPending = false;
	}
	
	public Account(ArrayList<Integer> names , int val, boolean pending, int id) {
		users = new ArrayList<Integer>();
		aid = id;
		for(Integer s:names) {
			users.add(s);
		}
		amount = val;
		isPending = pending;
	}
	
	public Account(int id, int val, boolean pending) {
		aid = id;
		amount = val;
		isPending = pending;
		users = new ArrayList<Integer>();
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

	public ArrayList<Integer> getUsers() {
		return users;
	}

	public void setUsers(ArrayList<Integer> users) {
		this.users = users;
	}
	
	public void addUser(Integer s) {
		users.add(s);
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
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
