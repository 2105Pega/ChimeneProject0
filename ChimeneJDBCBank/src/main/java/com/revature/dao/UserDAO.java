package com.revature.dao;

import java.util.List;

import com.revature.beans.User;

public interface UserDAO {
	public boolean addUser(User u);
	public boolean removeUser(User u);
	public List<User> getListOfUsers();
	public User getUserByName(String name);
	public String getNameByID(int id);
	public boolean updateUser(User oldUser, User newUser);
}
