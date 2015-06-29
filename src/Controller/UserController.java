package Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.User;

public class UserController extends DB_Controller {

	public UserController() {
		super();

	}

	// Add user
	public int addUser(User u) {

		int id = 0;

		String sql = "INSERT INTO User (FirstName,LastName,Role,UserName,PassWord) " + "VALUES('"
				+ u.getFirst_name() + "', '" + u.getLast_name() + "', '"
				+ u.getRole() + "', '" + u.getUserName() + "', '" + u.getPassWord() + "');";

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
			
			if(res.next()){
				u= new User(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
			}
			c.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return u;
	}
	
	public User getUserByName(String userName){

		String sql = "SELECT * FROM User WHERE UserName = '" + userName + "';";
		ResultSet res;
		User u = null;
		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			if(res.next()){
				u= new User(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
			}
			c.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return u;
	}
	
	public List<User> getUserByRole(String role){

		String sql = "SELECT * FROM User WHERE Role = '" + role + "';";
		ResultSet res;
		List<User> list = new ArrayList<User>();
		User u = null;
		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			while(res.next()){
				u= new User(res.getInt(1), res.getString(2), res.getString(3), res.getString(4), res.getString(5), res.getString(6));
				list.add(u);
			}
			c.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return list;
	}
}
