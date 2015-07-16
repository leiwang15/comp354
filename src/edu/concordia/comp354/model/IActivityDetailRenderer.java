package edu.concordia.comp354.model;

/**
 * Created by joao on 15-07-14.
 */
public interface IActivityDetailRenderer {
	/*
		set UI details from Activity
	 */
	void setActivityDetails(Activity activity);
	/*
		fill Activity with details from UI
	 */
	void fillActivityDetails(Activity activity);
}
