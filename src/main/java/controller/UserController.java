package controller;

import java.sql.SQLException;

import dao.UserDAO;
import model.User;

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
