package PERT;

import java.util.ArrayList;

/**
 * Created by joao on 15-05-26.
 */
public class PERT {

    ArrayList<PERTActivity> activities;

    public PERT() {
        this.activities = new ArrayList<PERTActivity>();
    }

    public void addActivity(PERTActivity activity) {
        activities.add(activity);
    }
}
