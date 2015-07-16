package edu.concordia.comp354.model;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.ProjectController;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.List;

public class Project {
    private int project_id;
    private String project_name;
    private String project_desc;
    private LocalDate start_date;
    private LocalDate end_date;
    private List<Activity> activities;

    //initialization with writing to DB
    public Project(User u, String name, String desc, LocalDate start, LocalDate end) {
        this.project_name = name;
        this.project_desc = desc;
        this.start_date = start;
        this.end_date = end;
        ProjectController pc = new ProjectController();
        this.project_id = pc.addProject(this, u);

    }

    //local initialization (usually read from DB)
    public Project(int projectID, String name, String desc, LocalDate start, LocalDate end, List<Activity> activities) {
        this.project_id = projectID;
        this.project_name = name;
        this.project_desc = desc;
        this.start_date = start;
        this.end_date = end;
        this.activities = activities;

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
     * @return the project_name
     */
    public String getProject_name() {
        return project_name;
    }

    /**
     * @param project_name the project_name to set
     */
    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    /**
     * @return the project_desc
     */
    public String getProject_desc() {
        return project_desc;
    }

    /**
     * @param project_desc the project_desc to set
     */
    public void setProject_desc(String project_desc) {
        this.project_desc = project_desc;
    }

    /**
     * @return the start_date
     */
    public LocalDate getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    /**
     * @return the end_date
     */
    public LocalDate getEnd_date() {
        return end_date;
    }

    /**
     * @param end_date the end_date to set
     */
    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    /**
     * @return the activities
     */
    public List<Activity> getActivities() {
        return activities;
    }

    /**
     * @param activities the activities to set
     */
    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }


    @Override
    public String toString() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String s = "Project ID: " +
                this.getProject_id()
                + " Project Name: "
                + this.getProject_name()
                + " Project Desc: "
                + this.getProject_desc()
                + " Start Date: "
                + df.format(this.start_date)
                + " End Date: "
                + df.format(this.end_date)
                + "\n";

        for (Activity activity : activities) {
            s += activity.toString();
        }
        return s;
    }

    public Object[][] getActivitityList() {
        Object[][] data = null;
        int i = 0;

        ActivityController ac = new ActivityController();
        List<Activity> list = ac.getActByProjectId(this.getProject_id());

        for (Activity a : list) {
            data[i][0] = a.getActivity_name();
            data[i][1] = a.getDuration();
            data[i][2] = a.getPredecessors();
            data[i][3] = a.getProgress();
//			data[i][4] = a.getFinished();
            i++;
        }

        return null;
    }
}
