package pm.Model;

import java.util.Date;
import java.util.List;

import pm.Controller.ProjectController;
public class Project {
    private int project_id;
    private String project_name;
    private String project_desc;
    private Date start_date;
    private Date end_date;
    private List<Activity> activities;

    
    public Project(User u, String name, String desc, Date start, Date end){
    	this.project_name = name;
    	this.project_desc = desc;
    	this.start_date = start;
    	this.end_date = end;
    	
    	ProjectController pc = new ProjectController();
    	this.project_id = pc.addProject(this, u);
    	
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
    public Date getStart_date() {
        return start_date;
    }

    /**
     * @param start_date the start_date to set
     */
    public void setStart_date(Date start_date) {
        this.start_date = start_date;
    }

    /**
     * @return the end_date
     */
    public Date getEnd_date() {
        return end_date;
    }

    /**
     * @param end_date the end_date to set
     */
    public void setEnd_date(Date end_date) {
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
}
