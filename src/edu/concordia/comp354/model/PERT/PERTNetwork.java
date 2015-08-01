package edu.concordia.comp354.model.PERT;

import edu.concordia.comp354.model.Activity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by joao on 15.07.26.
 */
public class PERTNetwork {

    public static final int UNDEFINED = -1;
    HashMap<Integer, EventLeg> activityID2EventLeg;
    int nextEventID;

    public HashMap<Integer, EventLeg> createNetwork(List<Activity> activities) {
        activityID2EventLeg = new HashMap<>();

        for (Activity activity : activities) {
            activityID2EventLeg.put(activity.getActivity_id(), new EventLeg());
        }

        nextEventID = 0;
        for (Activity activity : activities) {
            int n = UNDEFINED;

            if (activity.getPredecessors() != null) {
                for (Integer pred : activity.getPredecessors()) {

                    EventLeg eventLeg = activityID2EventLeg.get(pred);
                    if (eventLeg.destEvent == UNDEFINED) {
                        if (n == UNDEFINED) {
                            n = ++nextEventID;
                        }
                        eventLeg.destEvent = n;
                    }
                    else {
                        n = nextEventID;
                    }
                }
            } else {
                n = 0;
            }

            activityID2EventLeg.get(activity.getActivity_id()).orgEvent = n;
        }

        nextEventID++;
        for (EventLeg leg : activityID2EventLeg.values()) {
            if (leg.destEvent == UNDEFINED) {
                leg.destEvent = nextEventID;
            }
        }

        return activityID2EventLeg;
    }

    public int getNextEventID() {
        return nextEventID;
    }

    public void printNetwork(List<Activity> activities) {

        for (Activity activity : activities) {
            EventLeg leg = activityID2EventLeg.get(activity.getActivity_id());
            System.out.println(activity.getActivity_name() + "," + leg.getOrgEvent() + "," + leg.getDestEvent());
        }
    }
}

