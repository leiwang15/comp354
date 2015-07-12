package edu.concordia.comp354.model2.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.concordia.comp354.model2.model.User;

public class UserDAO extends SQLDAO {

	public UserDAO(DataSource ds) {
		super(ds);
	}

	public User add(User u, char[] password) throws SQLException {
		final String sql = "INSERT INTO User(FirstName, LastName, Role, UserName, Password) VALUES (?, ?, ?, ?, ?)";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
			st.setString(1, u.firstname);
			st.setString(2, u.lastname);
			st.setString(3, u.role.code);
			st.setString(4, u.username);
			st.setString(5, new String(password));
			st.executeUpdate();
			try (ResultSet rs = st.getGeneratedKeys()) {
				rs.next();
				final int userID = rs.getInt(1);
				return new User(userID, u.firstname, u.lastname, u.role, u.username);
			}
		}
	}

	public void delete(User u) throws SQLException {
		final String sql = "DELETE FROM User WHERE userID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, u.userID);
			st.executeUpdate();
		}
	}

	public User get(int userID) throws SQLException {
		final String sql = "SELECT FirstName, LastName, Role, UserName FROM User WHERE userID=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setInt(1, userID);
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) return null;
				final String firstname = rs.getString(1);
				final String lastname = rs.getString(2);
				final User.Role role = User.Role.codeFor(rs.getString(3));
				final String username = rs.getString(4);
				return new User(userID, firstname, lastname, role, username);
			}
		}
	}

	public User get(String username, char[] password) throws SQLException {
		final String sql = "SELECT UserId, FirstName, LastName, Role FROM User WHERE UserName=? AND PassWord=?";
		try (Connection c = getConnection(); PreparedStatement st = c.prepareStatement(sql)) {
			st.setString(1, username);
			st.setString(2, new String(password));
			try (ResultSet rs = st.executeQuery()) {
				if (!rs.next()) return null;
				final int userID = rs.getInt(1);
				final String firstname = rs.getString(2);
				final String lastname = rs.getString(3);
				final User.Role role = User.Role.codeFor(rs.getString(4));
				return new User(userID, firstname, lastname, role, username);
			}
		}
	}

}
