package pm.database;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		UserController us =  new UserController();
		int id = us.addUser("Lei", "Wang", "PM");
		System.out.println(id);
		
		
		UserController us2 =  new UserController();
		int f = us2.deleteUser(5);
		System.out.println(f);
		
		UserController us3 =  new UserController();
		f = us3.deleteUser("Lei", "Wang");
		System.out.println(f);
		
		
		
	}

}
