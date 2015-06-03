package pm.Controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import pm.Model.Project;
import pm.Model.User;

public class ProjectController extends DB_Controller {

	public ProjectController() {
		super();
	}

	public int addProject(Project p, User u) {

		int id = 0;
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String sql = "INSERT INTO Project (Name,Desc,StartDate,EndDate) "
				+ "VALUES('" + p.getProject_name() + "', '"
				+ p.getProject_desc() + "', '" + df.format(p.getStart_date())
				+ "', '" + df.format(p.getEnd_date()) + "');";

		System.out.println(sql);

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

}
