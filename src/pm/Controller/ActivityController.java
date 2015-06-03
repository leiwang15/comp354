package pm.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;

import pm.Model.Activity;
import pm.Model.User;

public class ActivityController extends DB_Controller {

	public ActivityController() {
		super();
	}

	/**
	 * @param  Activity a
	 * @return status assigned activity id
	 */
	public int addActivity(Activity activity) {

		int id = 0;

		String sql = "INSERT INTO Activity (Project_ID,Name,Desc,Duration) "
				+ "VALUES(" + activity.getProject_id() + ", '"
				+ activity.getActivity_name() + "', '"
				+ activity.getActivity_desc() + "', " + activity.getDuration()
				+ ");";

		String sql2 = "SELECT last_insert_rowid() FROM Activity;";

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

	/**
	 * @param User u, Activity a
	 * @return status 1 for successful
	 */
	public int assignUserToActivity(User u, Activity a) {

		int status = 0;
		String sql = "INSERT INTO Activity_Assign (Project_ID, User_ID)"
				+ "VALUES(" + a.getActivity_id() + ", " + u.getUser_id() + ")";
		try {
			st = c.createStatement();
			status = st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;
	}

}
