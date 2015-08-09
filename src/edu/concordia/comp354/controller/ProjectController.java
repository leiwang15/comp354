package edu.concordia.comp354.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.User;

public class ProjectController extends DB_Controller {

	public ProjectController() {
		super();
	}

	public ProjectController(String dbPath) {
		super(dbPath);
	}

	public int addProject(Project p, User u) {

		int id = 0;
		String sql = "INSERT INTO Project (Name,Desc,StartDate,EndDate) "
				+ "VALUES('" + p.getProject_name() + "', '"
				+ p.getProject_desc() + "', '" + p.getStart_date().format(df)
				+ "', '" + p.getEnd_date().format(df) + "');";

		try {
			st = c.createStatement();
			int res = st.executeUpdate(sql);
			// if insert successful
			if (res == 1) {
				// get project id
				st = c.createStatement();
				String sql2 = "SELECT last_insert_rowid() FROM Project;";
				ResultSet result = st.executeQuery(sql2);
				result.next();
				id = result.getInt(1);

				// Assign the current project to a user
				String sql3 = "INSERT INTO Project_Assign (Project_ID, User_ID) "
						+ "VALUES(" + id + ", " + u.getUser_id() + ");";

				st = c.createStatement();
				st.executeUpdate(sql3);

			} else {
				id = -9;
			}

			c.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// return assigned project id
		return id;
	}

	public void updateProject(Project p){
		String sql = "update Project set Name = '" + p.getProject_name() +
				"',Desc = '" + p.getProject_desc() + "',StartDate = '" +
				p.getStart_date().format(df) + "',EndDate = '" +
				p.getEnd_date().format(df) + "' where ProjectID = "+ p.getProject_id() + ";";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}

	public List<Project> getProjectsByUser(User u) {

		List<Project> lp = new ArrayList<Project>();

		String sql = "SELECT P.* FROM Project P "
				+ "INNER JOIN Project_Assign A ON P.ProjectID = A.Project_ID "
				+ "WHERE A.User_ID = " + u.getUser_id();

		ResultSet res;
		int id = 0;
		String name = "";
		String desc = "";
		LocalDate startDate;
		LocalDate endDate;
		int finished = 0;
		Project temp;
		try {

			// get projects
			st = c.createStatement();
			res = st.executeQuery(sql);
			while (res.next()) {
				id = res.getInt("ProjectID");
				name = res.getString("Name");
				desc = res.getString("Desc");
				LocalDate d =  LocalDate.parse(res.getString("StartDate"));

				//System.out.println(df.format(d));
				startDate = d;

				d = LocalDate.parse(res.getString("EndDate"));
				endDate = d;
				finished = res.getInt("Finished");
				// get activities for this project
				ActivityController ac = new ActivityController();
				List<Activity> la = ac.getActByProjectId(id);
				// save project to arraylist
				temp = new Project(id, name, desc, startDate, endDate, la);
				lp.add(temp);
			}

			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return lp;

	}

	public Project getProjectByID(int id) {
		String sql = "SELECT * FROM Project "
				+ "WHERE ProjectID = " + id;

		ResultSet res;
		String name = "";
		String desc = "";
		LocalDate startDate;
		LocalDate endDate;
		int finished = 0;
		Project p = null;
		try {

			// get projects
			st = c.createStatement();
			res = st.executeQuery(sql);
			while (res.next()) {
				name = res.getString("Name");
				desc = res.getString("Desc");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				LocalDate d =  LocalDate.parse(res.getString("StartDate"));

				//System.out.println(df.format(d));
				startDate = d;

				d = LocalDate.parse(res.getString("EndDate"));
				endDate = d;
				finished = res.getInt("Finished");
				// get activities for this project
				ActivityController ac = new ActivityController();
				List<Activity> la = ac.getActByProjectId(id);

				p = new Project(id, name, desc, startDate, endDate, la);
			}

			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return p;

	}

	public void deleteProject(int id){
		String sql = "DELETE FROM Project WHERE ProjectID = " + id +  ";";

		try {
			st = c.createStatement();
			st.executeUpdate(sql);
			c.close();

		} catch (SQLException e) {

			e.printStackTrace();
		}
	}
}
