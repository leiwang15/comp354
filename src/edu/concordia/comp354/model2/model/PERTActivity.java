package edu.concordia.comp354.model2.model;

public class PERTActivity extends Activity {
	public final int pessimistic;
	public final int optimistic;
	public final int normal;

	public PERTActivity(int activityID, String name, String description, int pessimistic, int optimistic, int normal) {
		super(activityID, name, description);
		this.pessimistic = pessimistic;
		this.optimistic = optimistic;
		this.normal = normal;
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
