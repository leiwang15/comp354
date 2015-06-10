package dbControl;

import Model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserController extends DB_Controller {

	public UserController() {
		super();

	}

	// Add user
	public int addUser(User u) {
		
		String userName = "";
		
		String sql = "INSERT INTO User (UserName,FirstName,LastName,Role,Password) " + "VALUES('"
				+ u.getUserName() + "','"
				+ u.getFirst_name() + "', '" + u.getLast_name() + "', '"
				+ u.getRole() + "' , '" + u.getPassword() + "');";

		String sql2 = "SELECT last_insert_rowid() FROM User;";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			st = c.createStatement();
			ResultSet result = st.executeQuery(sql2);
			result.next();
			userName = result.getString(1);
			c.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		
		if(userName.equals(u.getUserName())){
			return 1;
		}
		return 0;
	}

	// Delete a user according to user id
	public int deleteUser(int userName) {

		int feedback = 0;

		String sql = "DELETE FROM User WHERE UserName=" + userName + ";";

		try {
			st = c.createStatement();
			feedback = st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
		return feedback;
	}

	// Delete a user by first name and last name
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

	// Delete a user by providing a user object
	public int deleteUser(User u) {

		int feedback = 0;

		String sql = "DELETE FROM User WHERE UserName=" + u.getUserName() + ";";

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

	public User getUserByID(String userName){

		String sql = "SELECT * FROM User WHERE UserName = " + userName + ";";
		ResultSet res;
		User u = null;
		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			res.next();
			u= new User(res.getString(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5));
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return u;
	}
}
