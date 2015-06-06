package pm.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import pm.Model.User;

public class UserController extends DB_Controller {

	public UserController() {
		super();

	}

	// Add user
	public int addUser(User u) {

		int id = 0;

		String sql = "INSERT INTO User (FirstName,LastName,Role) " + "VALUES('"
				+ u.getFirst_name() + "', '" + u.getLast_name() + "', '"
				+ u.getRole() + "');";

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

			e.printStackTrace();
		}
		return id;
	}

	// Delete a user according to user id
	public int deleteUser(int user_id) {

		int feedback = 0;

		String sql = "DELETE FROM User WHERE UserId=" + user_id + ";";

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

		String sql = "DELETE FROM User WHERE UserId=" + u.getUser_id() + ";";

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
	
	public User getUserByID(int uid){
		
		String sql = "SELECT * FROM User WHERE UserID = " + uid + ";";
		ResultSet res;
		User u = null;
		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			res.next();
			u= new User(res.getInt(1), res.getString(2), res.getString(3), res.getString(4));
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return u;
	}
}
