package com.revature.beans;

import java.sql.Timestamp;

public class Transaction {
	private int accFrom;
	private int accTo;
	private int amount;
	private int type;
	private Timestamp ts;
	
	public Transaction(int accFrom, int accTo, int amount, int type, Timestamp ts) {
		super();
		this.accFrom = accFrom;
		this.accTo = accTo;
		this.amount = amount;
		this.type = type;
		this.ts = ts;
	}

	public int getAccFrom() {
		return accFrom;
	}

	public void setAccFrom(int accFrom) {
		this.accFrom = accFrom;
	}

	public int getAccTo() {
		return accTo;
	}

	public void setAccTo(int accTo) {
		this.accTo = accTo;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public Timestamp getTs() {
		return ts;
	}

	public void setTs(Timestamp ts) {
		this.ts = ts;
	}

	public String toString() {
		switch(type) {
		case 1: //Withdrawal
			return ts.toString() + " $" + amount + " was withdrawn from " + accFrom;
		case 2: //Deposit
			return ts.toString() + " $" + amount + " was deposited into " + accTo;
		case 3: //Transfer
			return ts.toString() + " $" + amount + " was transferred from " + accFrom + " to " + accTo;
		}
			
		return ts.toString() + " accFrom: " + accFrom + " accTo" + accTo + " amount: " + amount + " type: " + type;
	}
}
