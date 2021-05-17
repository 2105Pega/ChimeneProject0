package com.revature.user;

import java.util.ArrayList;

public class User implements java.io.Serializable{
	private boolean isEmployee;
	private String username;
	private String password; 
	private ArrayList<Integer> accIDs;
	
	public User() {
		isEmployee = false;
		username = "";
		password = "";
		accIDs = new ArrayList<Integer>();
	}
	
	public User(boolean emp, String user, String pass, ArrayList<Integer> ids) {
		isEmployee = emp;
		username = user;
		password = pass;
		accIDs = new ArrayList<Integer>();
		for(int i = 0; i < ids.size(); i++) {
			accIDs.add(ids.get(i));
		}
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEmployee() {
		return isEmployee;
	}

	public void setEmployee(boolean isEmployee) {
		this.isEmployee = isEmployee;
	}

	public ArrayList<Integer> getAccounts() {
		return accIDs;
	}
	
	public int getAccByID(int index) {
		return accIDs.get(index);
	}
	
	public void addAccount(int id) {
		accIDs.add(id);
	}
	
	public void removeAccount(int id) {
		int index = accIDs.indexOf(id);
		if(index > -1) {
			accIDs.remove(index);
		}
	}
	
	public String toString() {
		StringBuilder output = new StringBuilder();
		output.append("Username: " + username + " ");
		output.append("Password: " + password);
		return output.toString();
	}
}
