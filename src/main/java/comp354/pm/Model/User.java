package comp354.pm.Model;
import comp354.pm.Controller.UserController;
public class User {
	private int user_id;
    private String first_name;
    private String last_name;
    private String role;


    public User(String first_name, String last_name, String role) {
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;

		UserController uc = new UserController();
		this.user_id = uc.addUser(this);

	}


    public User(int uId, String first_name, String last_name, String role) {
		this.user_id = uId;
    	this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;

	}
    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the last_name
     */
    public String getLast_name() {
        return last_name;
    }

    /**
     * @param last_name the last_name to set
     */
    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    /**
     * @return the first_name
     */
    public String getFirst_name() {
        return first_name;
    }

    /**
     * @param first_name the first_name to set
     */
    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

	/**
	 * @return the user_id
	 */
	public int getUser_id() {
		return user_id;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

}
