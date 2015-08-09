package edu.concordia.comp354.model;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.model.EVA.EarnedValueAnalysis;
import edu.concordia.comp354.model.EVA.EarnedValuePoint;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Project extends DirtyAware {

    private int project_id;
    private String project_name;
    private String project_desc;
    private LocalDate start_date;
    private LocalDate end_date;
    private List<Activity> activities;
    private List<Integer> activityDeleteList;
    private List<EarnedValuePoint> EVAPoints;

    //initialization with writing to DB
    public Project(User u, String name, String desc, LocalDate start, LocalDate end) {
        super(DirtyLevels.UNTOUCHED);

        this.project_name = name;
        this.project_desc = desc;
        this.start_date = start;
        this.end_date = end;
        ProjectController pc = new ProjectController();
        this.project_id = pc.addProject(this, u);

        activityDeleteList = new ArrayList<>();
    }

    //local initialization (usually read from DB)
    public Project(int projectID, String name, String desc, LocalDate start, LocalDate end, List<Activity> activities) {
        super(DirtyLevels.UNTOUCHED);

        this.project_id = projectID;
        this.project_name = name;
        this.project_desc = desc;
        this.start_date = start;
        this.end_date = end;
        this.activities = activities;

        activityDeleteList = new ArrayList<>();
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
     * @return the project_name
     */
    public String getProject_name() {
        return project_name;
    }

    /**
     * @param project_name the project_name to set
     */
    public void setProject_name(String project_name) {

        if (project_name != null && !project_name.equals(this.project_name)) {
            this.project_name = project_name;
            changed();
        }
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
        if (project_desc != null && !project_name.equals(this.project_desc)) {
            this.project_desc = project_desc;
            changed();
        }
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
        if (start_date != null && !project_name.equals(this.start_date)) {
            this.start_date = start_date;
            changed();
        }
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
        if (end_date != null && !end_date.equals(this.end_date)) {
            this.end_date = end_date;
            changed();
        }
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
        if (activities != null && !activities.equals(this.activities)) {
            this.activities = activities;
            changed();
        }
    }

    @Override
    public boolean isDirty() {

        boolean dirty = super.isDirty();

        if (!dirty) {
            for (Activity activity : activities) {
                if (activity.isDirty()) {
                    dirty = true;
                    break;
                }
            }
        }

        return dirty;
    }

    @Override
    public String toString() {
//        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        String s = "Project ID: " +
                this.getProject_id()
                + " Project Name: "
                + this.getProject_name()
                + " Project Desc: "
                + this.getProject_desc()
                + " Start Date: "
                + this.start_date.format(df)
                + " End Date: "
                + this.end_date.format(df)
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

    public void addActivity() {
        activities.add(new Activity(project_id, "", "", 0, new ArrayList<Integer>(), 0, 0, 0));
    }

    public void deleteActivity(int activityID) {
        activities.remove(activityID - 1);
        //todo: notify subscriber of deletion
    }

    public void flagDeletedActivity(Activity deletedAct) {
        activityDeleteList.add(deletedAct.getDBID());
    }

    public List<Integer> getActivityDeleteList() {
        return activityDeleteList;
    }

    public boolean updateEVAPoint(EarnedValuePoint point) {
        if (EVAPoints != null) {
            for (int i = 0; i < EVAPoints.size(); i++) {
                if (EVAPoints.get(i).getDate() == point.getDate()) {
                    EVAPoints.set(i, point);
                    changed();
                    return true;
                }
            }
        }
        return false;
    }

    public List<EarnedValuePoint> getEVAPoints() {
        return EVAPoints;
    }

    public void generateEVAList(ActivityNetwork activityNetwork, int period) {

        EVAPoints = new ArrayList<>();

        float duration = activityNetwork.getProjectDuration();

        for (int date = period + 1; date < duration; date += period) {
            EVAPoints.add(new EarnedValuePoint(new EarnedValueAnalysis(activityNetwork, date, 0)));
        }

        setDirty(DirtyLevels.NEW);
    }

    public void setEVAPoints(List<EarnedValuePoint> EVAPoints) {
        this.EVAPoints = EVAPoints;
    }
}
