package edu.concordia.comp354.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.User;


public class ActivityController extends DB_Controller {

	public ActivityController() {
		super();
	}

	/**
	 * @param  activity
	 * @return status assigned activity id
	 */
	public int addActivity(Activity activity) {

		int id = 0;

		String sql = "INSERT INTO Activity (Project_ID,Name,Desc,Duration,Pessimistic,Optimistic,Value) "
				+ "VALUES('" + activity.getProject_id() + "', '"
				+ activity.getActivity_name() + "', '"
				+ activity.getActivity_desc() + "', '" + activity.getDuration() + "', '" + activity.getPessimistic() +"', '"
				+ activity.getOptimistic() +"', '"  + activity.getCost()
				+ "');";

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

	public void updateActivity(Activity activity){

		String sql = "update Activity set Name = '" + activity.getActivity_name() +
				"',Desc = '" + activity.getActivity_desc() + "',Duration = " +
				activity.getDuration() + ",Pessimistic = " + activity.getPessimistic() +
				",Optimistic = " + activity.getOptimistic() + ",Value = " + activity.getCost() +
				" where ActivityID = "+ activity.getDBID() + ";";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void updateActivity(int id, int progress, String des){
		String sql = "update Activity set Desc = '" + des + "',Progress = " + progress + " where ActivityID = "+ id + ";";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param u, Activity a
	 * @return status 1 for successful
	 */
	public int assignUserToActivity(User u, Activity a) {

		int status = 0;
		String sql = "INSERT INTO Activity_Assign (Activity_Id, User_ID)"
				+ "VALUES(" + a.getDBID() + ", " + u.getUser_id() + ")";
		try {
			st = c.createStatement();
			status = st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		a.written();
		return status;
	}

	public List<Integer> getUserByAssignment(Activity a){
		String sql = "Select * FROM Activity_Assign WHERE Activity_ID = '" + a.getDBID() + "';";
		ResultSet res;
		List<Integer> list = new ArrayList<Integer>();

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			if(res != null){
				while (res.next()) {
					list.add(res.getInt("User_ID"));
				}
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public List<Integer> getActByUser(int uId){
		String sql = "Select * FROM Activity_Assign WHERE User_ID = '" + uId + "';";
		ResultSet res;
		List<Integer> list = new ArrayList<Integer>();

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			if(res != null){
				while (res.next()) {
					list.add(res.getInt("Activity_ID"));
				}
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void removeUserFromActivity(Activity a, int uid){
		String sql = "DELETE FROM Activity_Assign WHERE User_ID = '" + uid +"' AND Activity_Id = '" + a.getDBID() +"';";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
	/**
	 * @param aID, Activity pre
	 * @return status 1 for successful
	 */
	public int setActPredecessor(int aID, int preID){
		int status = 0;
		String sql = "INSERT INTO Activity_Pre (Activity_ID1, Activity_ID2)"
				+ "VALUES(" + aID + ", " + preID + ")";
		try {
			st = c.createStatement();
			status = st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return status;


	}

	public List<Integer> getActPredecessors(int id){
		String sql = "SELECT * FROM Activity_Pre " + "WHERE Activity_ID1 = " + id + ";";
		ResultSet res;
		List<Integer> list = new ArrayList<Integer>();

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);
			if(res != null){
				while (res.next()) {
					list.add(res.getInt("Activity_ID2"));
				}
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return list;
	}

	public void removePredecessor(int id){
		String sql = "DELETE FROM Activity_Pre WHERE Activity_ID1 = " + id + ";";
		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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
		int pess = 0;
		int opt = 0;
		int value = 0;

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);

			while (res.next()) {
				id = res.getInt("ActivityID");
				name = res.getString("Name");
				desc = res.getString("Desc");
				duration = res.getInt("Duration");
				progress = res.getInt("Progress");
				finished = res.getInt("Finished");
				pess = res.getInt("Pessimistic");
				opt = res.getInt("Optimistic");
				value = res.getInt("Value");
				a = new Activity(id, pID, name, desc, duration, progress, finished,null,pess,opt,value);
				la.add(a);
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return la;
	}

	public List<Activity> getActByActId (int aID){

		String sql = "SELECT * FROM Activity "
				+ "WHERE ActivityID = "	+ aID + ";";

		ResultSet res;
		List<Activity> la = new ArrayList<Activity>();
		Activity a;
		int id =0;
		String name = "";
		String desc = "";
		int duration = 0;
		int progress =0;
		int finished =0;
		int pess = 0;
		int opt = 0;
		int value = 0;

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);

			while (res.next()) {
				id = res.getInt("Project_ID");
				name = res.getString("Name");
				desc = res.getString("Desc");
				duration = res.getInt("Duration");
				progress = res.getInt("Progress");
				finished = res.getInt("Finished");
				pess = res.getInt("Pessimistic");
				opt = res.getInt("Optimistic");
				value = res.getInt("Value");
				a = new Activity(aID, id, name, desc, duration, progress, finished,null,pess,opt,value);
				la.add(a);
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return la;
	}

	public Activity getActByActName (String name){

		String sql = "SELECT * FROM Activity "
				+ "WHERE Name = '"	+ name + "';";

		ResultSet res;
		List<Activity> la = new ArrayList<Activity>();

		int id =0;
		int aID = 0;
		String desc = "";
		int duration = 0;
		int progress =0;
		int finished =0;
		int pess = 0;
		int opt = 0;
		int value = 0;

		try {
			st = c.createStatement();
			res = st.executeQuery(sql);

			while (res.next()) {
				id = res.getInt("Project_ID");
				aID = res.getInt("ActivityID");
				desc = res.getString("Desc");
				duration = res.getInt("Duration");
				progress = res.getInt("Progress");
				finished = res.getInt("Finished");
				pess = res.getInt("Pessimistic");
				opt = res.getInt("Optimistic");
				value = res.getInt("Value");
				Activity a = new Activity(aID, id, name, desc, duration, progress, finished,null,pess,opt,value);
				c.close();
				return a;
			}
			c.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;

	}

	public void deleteAct(int id){
		String sql = "DELETE FROM Activity WHERE ActivityID = " + id + ";";
		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

//	public void deletePredecessor(int id){
//		String sql = "DELETE FROM Activity_Pre WHERE Activity_ID1 = " + id + " OR Activity_ID2 = " + id + ";";
//		try {
//			st = c.createStatement();
//			st.executeUpdate(sql);
//			c.close();
//
//		} catch (SQLException e) {
//
//			e.printStackTrace();
//		}
//	}

	public void severPredecessorsFromActivity(int dbID){
		String sql = "DELETE FROM Activity_Pre WHERE Activity_ID1 = " + dbID + ";";
		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void deleteAssign(int id){
		String sql = "DELETE FROM Activity_Assign WHERE Activity_ID = " + id + ";";
		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public void deleteActByProject(int id){
		String sql = "DELETE FROM Activity_Assign WHERE Project_ID = " + id + ";";
		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

}
