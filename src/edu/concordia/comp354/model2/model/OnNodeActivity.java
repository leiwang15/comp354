package edu.concordia.comp354.model2.model;

public class OnNodeActivity extends Activity {
	public final int duration;

	public OnNodeActivity(int activityID, String name, String description, int duration) {
		super(activityID, name, description);
		this.duration = duration;
	}
}
