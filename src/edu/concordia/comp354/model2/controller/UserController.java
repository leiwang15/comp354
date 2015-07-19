package edu.concordia.comp354.model2.controller;

import edu.concordia.comp354.model2.dao.UserDAO;
import edu.concordia.comp354.model2.model.User;

import java.sql.SQLException;

public class UserController {

	private UserDAO userDAO;

	public UserController(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

	public User add(User u, char[] password) throws SQLException {
		return userDAO.add(u, password);
	}

	public void delete(User u) throws SQLException {
		userDAO.delete(u);
	}

	public User login(String username, char[] password) throws SQLException {
		final User u = userDAO.get(username, password);
		if (u == null) {
			// TODO handle user not found
		}
		return u;
	}
}
