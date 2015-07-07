package model;

public class Project {
	public final int projectID;
	public final String name;
	public final String description;

	public Project(int projectID, String name, String description) {
		this.projectID = projectID;
		this.name = name;
		this.description = description;
	}
}
