package pm.Controller;

import java.util.ArrayList;
import java.util.Calendar;




import java.util.List;

import pm.Model.Activity;
import pm.Model.Project;
import pm.Model.User;

public class test {


	public static void main(String[] args) {
		// TODO Auto-generated method stub

		User u1 = new User("Lei", "Wang", "PM");
		System.out.println("User id: " + u1.getUser_id());


//		UserController us2 =  new UserController();
//		int f = us2.deleteUser(5);
//		System.out.println(f);
//
//		UserController us3 =  new UserController();
//		f = us3.deleteUser("Lei", "Wang");
//		System.out.println(f);

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, 06);
		cal.set(Calendar.DAY_OF_MONTH, 03);
		Calendar cal2 = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2015);
		cal.set(Calendar.MONTH, 06);
		cal.set(Calendar.DAY_OF_MONTH, 15);

		Project p1 = new Project(u1, "P1", "P1 desc", cal.getTime() , cal2.getTime());
		System.out.println("Project id: " + p1.getProject_id());

		Activity a1 = new Activity(p1.getProject_id(), "A1", "A1 desc", 1, new ArrayList<Integer>());
		System.out.println("Activity id: " + a1.getActivity_id());

		Activity a2 = new Activity(p1.getProject_id(), "A2", "A2 desc", 2, new ArrayList<Integer>());
		System.out.println("Activity id: " + a2.getActivity_id());

		Activity a3 = new Activity(p1.getProject_id(), "A2", "A2 desc", 3, new ArrayList<Integer>());
		System.out.println("Activity id: " + a3.getActivity_id());

		Activity a4 = new Activity(p1.getProject_id(), "A2", "A2 desc", 4, new ArrayList<Integer>());
		System.out.println("Activity id: " + a4.getActivity_id());

		Activity a5 = new Activity(p1.getProject_id(), "A2", "A2 desc", 5, new ArrayList<Integer>());
		System.out.println("Activity id: " + a5.getActivity_id());

		Activity a6 = new Activity(p1.getProject_id(), "A2", "A2 desc", 6, new ArrayList<Integer>());
		System.out.println("Activity id: " + a6.getActivity_id());

		Activity a7 = new Activity(p1.getProject_id(), "A2", "A2 desc", 7, new ArrayList<Integer>());
		System.out.println("Activity id: " + a7.getActivity_id());

		Activity a8 = new Activity(p1.getProject_id(), "A2", "A2 desc", 8, new ArrayList<Integer>());
		System.out.println("Activity id: " + a8.getActivity_id());

		ActivityController ac = new ActivityController();
		ac.assignUserToActivity(u1, a1);

		ActivityController ac2 = new ActivityController();
		ac2.setActPrecedence(a2, a1);

		ActivityController ac3 = new ActivityController();
		ac3.setActPrecedence(a3, a1);

		ActivityController ac4 = new ActivityController();
		ac4.setActPrecedence(a4, a2);

		ProjectController pc = new ProjectController();
		UserController uc = new UserController();
		List<Project> ap = new ArrayList<Project>();
		ap = pc.getProjectsByUser(uc.getUserByID(u1.getUser_id()));

		System.out.println(ap);
	}

}
