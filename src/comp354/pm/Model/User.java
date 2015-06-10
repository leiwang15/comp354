package comp354.pm.Model;
import comp354.pm.Controller.UserController;
public class User {
	private int user_id;
	private String userName;
    private String first_name;
    private String last_name;
    private String role;
    private String password;


    public User(int uId, String userName, String first_name, String last_name, String role, String password) {
		this.user_id = uId;
		this.userName = userName;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
		this.password = password;

		UserController uc = new UserController();
		this.user_id = uc.addUser(this);

	}

	public User(String userName, String firstName, String lastName, String role, String password) {
		this.userName = userName;
		this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
		this.password = password;

		UserController uc = new UserController();
		this.user_id = uc.addUser(this);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUser_id(int user_id) {
		this.user_id = user_id;
	}

	/**
	 * @param userName the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
