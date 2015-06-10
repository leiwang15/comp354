package Model;
public class User {
	private String userName;
    private String first_name;
    private String last_name;
    private String role;
    private String password;


//    public User(String first_name, String last_name, String role, String password) {
//		this.first_name = first_name;
//		this.last_name = last_name;
//		this.role = role;
//		this.password = password;
//		
//		UserController uc = new UserController();
//		this.userName = uc.addUser(this);
//
//	}


    public User(String uId, String first_name, String last_name, String role, String password) {
		this.userName = uId;
    	this.first_name = first_name;
		this.last_name = last_name;
		this.role = role;
		this.password = password;
	}
    public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
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
	public String getUserName() {
		return userName;
	}

	/**
	 * @param user_id the user_id to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

}
