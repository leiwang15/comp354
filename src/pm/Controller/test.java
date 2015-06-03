package pm.Controller;

import java.util.Calendar;



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
		
		Activity a1 = new Activity(p1.getProject_id(), "A1", "A1 desc", 3);
		System.out.println("Activity id: " + a1.getActivity_id());
	}

}
