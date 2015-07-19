package edu.concordia.comp354.model;

/**
 * Created by joao on 15-07-14.
 */
public interface IActivityDetailRenderer {
	/*
		set UI details from Activity
	 */
	void setUIDetailsFromActivity(Activity activity);
	/*
		fill Activity with details from UI
	 */
	void getActivityDetailsFromUI(Activity activity);
}
