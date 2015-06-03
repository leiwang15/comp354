package pm.database;

import java.io.File;

import java.sql.*;

public class UserController {

	private static Connection c = null;
	private static Statement st = null;
	private static String DBpath = "project.db";

	public UserController() {
		try {
			File f = new File(DBpath);
			if (f.exists()) {
				c = DriverManager.getConnection("jdbc:sqlite:" + DBpath);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	//Add user
	public int addUser(String first_name, String last_name, String role) {

		int id = 0;

		String sql = "INSERT INTO User (FirstName,LastName,Role) " + "VALUES('"
				+ first_name + "', '" + last_name + "', '" + role + "');";

		String sql2 = "SELECT last_insert_rowid() FROM User;";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			st = c.createStatement();
			ResultSet result = st.executeQuery(sql2);
			result.next();
			id = result.getInt(1);
			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	//Delete a user according to user id
	public int deleteUser(int user_id) {

		int feedback = 0;

		String sql = "DELETE FROM User WHERE UserId=" + user_id + ";";

		try {
			st = c.createStatement();
			feedback = st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feedback;
	}

	//Delete a user by first name and last name
	public int deleteUser(String first_name, String last_name) {

		int feedback = 0;

		String sql = "DELETE FROM User WHERE FirstName='" + first_name
				+ "' AND LastName='" + last_name + "';";

		try {
			st = c.createStatement();
			feedback = st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return feedback;
	}
}
