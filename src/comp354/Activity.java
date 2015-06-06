package comp354;


import java.util.ArrayList;

public class Activity {
    private int activity_id;
    private String activity_name;
    private String activity_desc;
    private int duration; //in hours
    private ArrayList<Integer> predecessors;

    public Activity() {
    }

    public Activity(int activity_id) {
        this.activity_id = activity_id;
        activity_name = "";
        activity_desc = "";
        duration = 0;
        predecessors = new ArrayList<Integer>();
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
}
