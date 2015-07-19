package edu.concordia.comp354.model2.model;

import org.joda.time.DateTime;

public class ActivityHistory {
	public final int userID;
	public final int progress;
	public final String comment;
	public final DateTime timestamp;

	public ActivityHistory(int userID, int progress, String comment, DateTime timestamp) {
		this.userID = userID;
		this.progress = progress;
		this.comment = comment;
		this.timestamp = timestamp;
	}

	public boolean isComplete() {
		return progress >= 100;
	}
}