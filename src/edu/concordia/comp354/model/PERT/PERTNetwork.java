package edu.concordia.comp354.model.PERT;

import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.ProjectManager;

import java.util.HashMap;
import java.util.List;

/**
 * Created by joao on 15.07.26.
 */
public class PERTNetwork {

    HashMap<Integer, EventEdge> activityID2EventLeg;
    int nextEventID;

    public HashMap<Integer, EventEdge> createNetwork(List<Activity> activities) {
        activityID2EventLeg = new HashMap<>();

        for (Activity activity : activities) {
            activityID2EventLeg.put(activity.getActivity_id(), new EventEdge());
        }

        nextEventID = 0;
        for (Activity activity : activities) {
            int n = ProjectManager.UNDEFINED;

            if (activity.getPredecessors() != null) {
                for (Integer pred : activity.getPredecessors()) {

                    EventEdge eventEdge = activityID2EventLeg.get(pred);
                    if (eventEdge.destEvent == ProjectManager.UNDEFINED) {
                        if (n == ProjectManager.UNDEFINED) {
                            n = ++nextEventID;
                        }
                        eventEdge.destEvent = n;
                    } else {
                        n = nextEventID;
                    }
                }
            } else {
                n = 0;
            }

            activityID2EventLeg.get(activity.getActivity_id()).orgEvent = n;
        }

        nextEventID++;
        for (EventEdge leg : activityID2EventLeg.values()) {
            if (leg.destEvent == ProjectManager.UNDEFINED) {
                leg.destEvent = nextEventID;
            }
            if (leg.orgEvent == ProjectManager.UNDEFINED) {
                leg.orgEvent = 0;
            }
        }

        return activityID2EventLeg;
    }

    public int getNextEventID() {
        return nextEventID;
    }

    public void printNetwork(List<Activity> activities) {

        for (Activity activity : activities) {
            EventEdge leg = activityID2EventLeg.get(activity.getActivity_id());
            System.out.println(activity.getActivity_name() + "," + leg.getOrgEvent() + "," + leg.getDestEvent());
        }
    }
}

