package comp354.pm.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import comp354.pm.Model.Activity;
import comp354.pm.Model.User;


public class ActivityController extends DB_Controller {

	public ActivityController() {
		super();
	}

	/**
	 * @param  activity (Joao: Removed Activity a to pass compile validations for now)
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
	 * @param u, Activity a
	 * @return status 1 for successful
	 */
	public int assignUserToActivity(User u, Activity a) {

		int status = 0;
		String sql = "INSERT INTO Activity_Assign (Activity_Id, User_ID)"
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

	/**
	 * @param a, Activity pre
	 * @return status 1 for successful
	 */
	public int setActPrecedence(Activity a, Activity pre){
		int status = 0;
		String sql = "INSERT INTO Activity_Pre (Activity_ID1, Activity_ID2)"
				+ "VALUES(" + a.getActivity_id() + ", " + pre.getActivity_id() + ")";
		try {
			st = c.createStatement();
			status = st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;


	}


	public List<Activity> getActByProjectId (int pID){

		String sql = "SELECT * FROM Activity "
				+ "WHERE Project_ID = "	+ pID + ";";

		ResultSet res;
		List<Activity> la = new ArrayList<Activity>();
		Activity a;
		int id =0;
		String name = "";
		String desc = "";
		int duration = 0;
		int progress =0;
		int finished =0;

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);

			while (res.next()) {
				id = res.getInt("ActivityID");
				name = res.getString("Name");
				desc = res.getString("Desc");
				duration = res.getInt("Duration");
				finished = res.getInt("Finished");
				a = new Activity(id, pID, name, desc, duration, progress, finished,null);
				la.add(a);
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}


		return la;


	}

}
