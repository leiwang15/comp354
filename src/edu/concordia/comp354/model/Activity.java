package edu.concordia.comp354.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Activity extends DirtyAware {
    private int dbID;
    private int activity_id;
    private int project_id;
    private String activity_name;
    private String activity_desc;
    private int progress;
    private int duration; //in days
    private int pessimistic;
    private int optimistic;
    private int value;
    private List<Integer> predecessors;
    private List<User> users;

    public Activity() {
        super(DirtyLevels.NEW);
    }

    //initialization with writing to DB
    public Activity(int project_id, String name, String desc, int duration, List<Integer> predecessors, int pessimistic, int optimistic, int value) {
        super(DirtyLevels.NEW);

        this.dbID = -1;
        this.project_id = project_id;
        this.activity_name = name;
        this.activity_desc = desc;
        this.duration = duration;
        this.predecessors = predecessors;
        this.pessimistic = pessimistic;
        this.optimistic = optimistic;
        this.value = value;

        users = new ArrayList<>();
    }

    //Local initialization without writing to DB
    public Activity(int dbID, int project_id, String name, String desc, int duration, int progress, int finished, List<Integer> predecessors, int pessimistic, int optimistic, int value) {
        super(DirtyLevels.UNTOUCHED);

        this.dbID = dbID;
        this.project_id = project_id;
        this.activity_name = name;
        this.activity_desc = desc;
        this.duration = duration;
        this.progress = progress;
        this.predecessors = predecessors;
        this.pessimistic = pessimistic;
        this.optimistic = optimistic;
        this.value = value;

        users = new ArrayList<>();
    }

    public int getDBID() {
        return dbID;
    }

    public void setDBID(int uiID) {
        this.dbID = uiID;
    }

    public int getPessimistic() {
        return pessimistic;
    }

    public void setPessimistic(int pessimistic) {

        if (pessimistic != this.pessimistic) {
            this.pessimistic = pessimistic;
            changed();
        }
    }

    public int getOptimistic() {
        return optimistic;
    }

    public void setOptimistic(int optimistic) {
        if (optimistic != this.optimistic) {
            this.optimistic = optimistic;
            changed();
        }
    }

    public int getCost() {
        return value;
    }

    public void setValue(int value) {

        if (value != this.value) {
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

        if (activity_id != this.activity_id) {
            this.activity_id = activity_id;
//            changed();
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

        if (activity_name != null && !activity_name.equals(this.activity_name)) {
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

        if (!activity_desc.equals(this.activity_desc)) {
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
        if (duration != this.duration) {
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
        if (progress != this.progress) {
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
        if (project_id != this.project_id) {
            this.project_id = project_id;
            changed();
        }
    }

    /**
     * @return the predecessors
     */
    public List<Integer> getPredecessors() {
        return this.predecessors;
    }

    /**
     * @param predecessors the predecessors
     */
    public void setPredecessors(List<Integer> predecessors) {

        if (!predecessors.equals(this.predecessors)) {
            this.predecessors = predecessors;
            changed();
        }
    }

    public float getExpectedDate() {
        return (optimistic + 4 * duration + pessimistic) / 6f;
    }

    public float getStdev() {
        return (pessimistic - optimistic) / 6f;
    }

    @Override
    public boolean equals(Object obj) {
        Activity a = (Activity) obj;
        return dbID == a.dbID &&
                activity_id == a.activity_id &&
                project_id == a.project_id &&
                activity_name.equals(a.activity_name) &&
                activity_desc.equals(a.activity_desc) &&
                progress == a.progress &&
                duration == a.duration &&
                pessimistic == a.pessimistic &&
                optimistic == a.optimistic &&
                value == a.value &&
                predecessors == a.predecessors || predecessors != null && a.predecessors != null && predecessors.equals(a.predecessors) &&
                users == a.users || users != null && a.users != null && users.equals(a.users);
    }

    @Override
    public String toString() {

        String s = "Activity ID: " +
                this.getActivity_id()
                + " DBID: "
                + this.getDBID()
                + " Project Name: "
                + this.getActivity_name()
                + " Project Desc: "
                + this.getActivity_desc()
                + " Optimistic: "
                + this.getOptimistic()
                + " Duration: "
                + this.getDuration()
                + " Pessimistic: "
                + this.getPessimistic()
                + " Value: "
                + this.getCost()
                + " Progress: "
                + this.getProgress()
                + " predecessors: "
                + Arrays.asList(predecessors)
                + " dirty: "
                + dirty
                + "\n";
        return s;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        if (!users.equals(this.users)) {
            this.users = users;
            changed();
        }
    }

}
