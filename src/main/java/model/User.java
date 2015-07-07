package model;

public class User {
	public final int userID;
	public final String firstname;
	public final String lastname;
	public final Role role;
	public final String username;

	public enum Role {
		MANAGER("M"), REGULAR("R");

		public String code;

		Role(String code) {
			this.code = code;
		}

		public static Role codeFor(String code) {
			return (code == "M") ? MANAGER : REGULAR;
		}
	}

	public User(int userID, String firstname, String lastname, Role role, String username) {
		this.userID = userID;
		this.firstname = firstname;
		this.lastname = lastname;
		this.role = role;
		this.username = username;
	}
}
