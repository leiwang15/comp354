package model;

public abstract class Activity {
	public final int activityID;
	public final String name;
	public final String description;

	public Activity(int activityID, String name, String description) {
		this.activityID = activityID;
		this.name = name;
		this.description = description;
	}
}
