package model;

public class PERTActivity extends Activity {
	public final int pessimistic;
	public final int optimistic;
	public final int estimated;

	public PERTActivity(int activityID, String name, String description, int pessimistic, int optimistic, int estimated) {
		super(activityID, name, description);
		this.pessimistic = pessimistic;
		this.optimistic = optimistic;
		this.estimated = estimated;
	}
	
	public double s() {
		// TODO
		return 0;
	}
	
	public double t() {
		// TODO
		return 0;
	}
}
