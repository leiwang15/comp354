package comp354;

import java.util.ArrayList;

public class ActivityList {

    ArrayList<Activity> activities;

    public ActivityList() {
        this.activities = new ArrayList<Activity>();
    }

    public ActivityList(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }
}