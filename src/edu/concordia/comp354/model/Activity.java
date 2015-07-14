package edu.concordia.comp354.model;

import edu.concordia.comp354.controller.ActivityController;
import java.util.ArrayList;
import java.util.Arrays;

public class Activity {
    private int activity_id;
    private int project_id;
    private String activity_name;
    private String activity_desc;
    private int progress;
    private int duration; //in days
    private int pessimistic;
    private int optimistic;
    private int value;
    private int finished;
    private ArrayList<Integer> predecessors;
	boolean dirty = false;

  //initialization with writing to DB
    public Activity (int project_id, String name, String desc, int duration, ArrayList<Integer> predecessors, int pessimistic, int optimistic, int value){
    	this.project_id = project_id;
    	this.activity_name = name;
    	this.activity_desc = desc;
    	this.duration = duration;
        this.predecessors = predecessors;
        this.pessimistic = pessimistic;
        this.optimistic = optimistic;
        this.value = value;

    	ActivityController ac = new ActivityController();
    	this.setActivity_id(ac.addActivity(this));

    	for(Integer i : predecessors){
    		ActivityController ac1 = new ActivityController();
    		ac1.setActPrecedence(this.getActivity_id(), i);
    	}
    }

	//Local initialization without writing to DB
	public Activity (int activity_id, int project_id, String name, String desc, int duration, int progress, int finished, ArrayList<Integer> predecessors, int pessimistic, int optimistic, int value){

		this.activity_id = activity_id;
		this.project_id = project_id;
		this.activity_name = name;
		this.activity_desc = desc;
		this.duration = duration;
		this.progress = progress;
		this.finished = finished;
		this.predecessors = predecessors;
		this.pessimistic = pessimistic;
		this.optimistic = optimistic;
		this.value = value;

	}
    public int getPessimistic() {
		return pessimistic;
	}

	public void setPessimistic(int pessimistic) {

		if ( pessimistic != this.pessimistic) {
			this.pessimistic = pessimistic;
			changed();
		}
	}

	public int getOptimistic() {
		return optimistic;
    }

	public void setOptimistic(int optimistic) {
		if ( optimistic != this.optimistic) {
			this.optimistic = optimistic;
			changed();
		}
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {

		if ( value != this.value) {
			this.value = value;
			changed();
		}
	}

    /**
     * @return the activity_id
     */
    public int getActivity_id() {
        return activity_id;
    }

    /**
     * @param activity_id the activity_id to set
     */
    public void setActivity_id(int activity_id) {

		if ( activity_id != this.activity_id) {
			this.activity_id = activity_id;
			changed();
		}
	}

    /**
     * @return the activity_name
     */
    public String getActivity_name() {
        return activity_name;
    }

    /**
     * @param activity_name the activity_name to set
     */
    public void setActivity_name(String activity_name) {

		if ( !activity_name.equals(this.activity_name)) {
			this.activity_name = activity_name;
			changed();
		}
	}

    /**
     * @return the activity_desc
     */
    public String getActivity_desc() {
        return activity_desc;
    }

    /**
     * @param activity_desc the activity_desc to set
     */
    public void setActivity_desc(String activity_desc) {

		if ( !activity_desc.equals(this.activity_desc)) {
			this.activity_desc = activity_desc;
			changed();
		}
	}

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
		if ( duration != this.duration) {
			this.duration = duration;
			changed();
		}
	}

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		if ( progress != this.progress) {
			this.progress = progress;
			changed();
		}
	}

	/**
	 * @return the project_id
	 */
	public int getProject_id() {
		return project_id;
	}

	/**
	 * @param project_id the project_id to set
	 */
	public void setProject_id(int project_id) {
		if ( project_id != this.project_id) {
			this.project_id = project_id;
			changed();
		}
	}

	private void changed() {
		dirty = true;
	}

	/**
     * @return the predecessors
     */
    public ArrayList<Integer> getPredecessors() {
        return this.predecessors;
    }

    /**
     * @param predecessors the predecessors
     */
    public void setPredecessors(ArrayList<Integer> predecessors) {

		if ( !predecessors.equals(this.predecessors)) {
			this.predecessors = predecessors;
			changed();
		}
	}

	@Override

	public String toString(){

		String s = "Activity ID: " +
		this.getActivity_id()
		+ " Project Name: "
		+ this.getActivity_name()
		+ " Project Desc: "
		+ this.getActivity_desc()
		+ " Duration: "
		+ this.getDuration()
		+ " Progress: "
		+ this.getProgress()
		+ " predecessors: "
		+ Arrays.asList(predecessors)
		+ "\n";
		return s;
	}
}
