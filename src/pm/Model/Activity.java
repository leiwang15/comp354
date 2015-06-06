package pm.Model;

import pm.Controller.ActivityController;
import java.util.ArrayList;
import java.util.Arrays;

public class Activity {
    private int activity_id;
    private int project_id;
    private String activity_name;
    private String activity_desc;
    private int progress;
    private int duration; //in hours
    private int finished;
    private ArrayList<Integer> predecessors;

  //initialization with writing to DB
    public Activity (int project_id, String name, String desc, int duration, ArrayList<Integer> predecessors){
    	this.project_id = project_id;
    	this.activity_name = name;
    	this.activity_desc = desc;
    	this.duration = duration;
        this.predecessors = predecessors;

    	ActivityController ac = new ActivityController();
    	this.setActivity_id(ac.addActivity(this));
    }

    //Local initialization without writing to DB
    public Activity (int activity_id, int project_id, String name, String desc, int duration, int progress, int finished){

    	this.activity_id = activity_id;
    	this.project_id = project_id;
    	this.activity_name = name;
    	this.activity_desc = desc;
    	this.duration = duration;
    	this.progress = progress;
    	this.finished = finished;


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
        this.activity_id = activity_id;
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
        this.activity_name = activity_name;
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
        this.activity_desc = activity_desc;
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
        this.duration = duration;
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
		this.progress = progress;
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
		this.project_id = project_id;
	}
	/**
	 * @return the finished
	 */
	public int getFinished() {
		return finished;
	}
	/**
	 * @param finished the finished to set
	 */
	public void setFinished(int finished) {
		this.finished = finished;
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
        this.predecessors = predecessors;
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
		+ " Is finished: "
		+ this.getFinished()
		+ " predecessors: "
		+ Arrays.asList(predecessors)
		+ "\n";
		return s;
	}
}
