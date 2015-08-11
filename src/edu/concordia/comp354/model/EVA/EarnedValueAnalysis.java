package edu.concordia.comp354.model.EVA;

import com.mxgraph.model.mxCell;
import edu.concordia.comp354.model.AON.ActivityOnNode;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.ActivityNetwork;

import java.time.LocalDate;
import java.util.Collection;

/**
 * Created by joao on 15.08.02.
 */
public class EarnedValueAnalysis {

    private final ActivityNetwork activityNetwork;
    private final int date;

    private final float AC; //  actual cost
    private float BAC;      //  budget at completion
    private float PV;       //  planned value
    private float EV;       //  earned value

    public EarnedValueAnalysis(ActivityNetwork activityNetwork, int date, float AC) {
        this.activityNetwork = activityNetwork;
        this.date = date;
        this.AC = AC;

        computeBAC();
        computePV();
        computeEV();
    }

    public float getAC() {
        return AC;
    }

    private void computeBAC() {

        BAC = 0;
        for (Activity activity : activityNetwork.getActivities()) {
            BAC += activity.getValue();
        }
    }

    private void computePV() {
        Collection<mxCell> list = activityNetwork.getActivityNodes();

        PV = 0;
        for (mxCell cell : list) {
            ActivityOnNode node = (ActivityOnNode) cell.getValue();

            PV += node.getPV(date);
        }
    }

    private void computeEV() {
        Collection<mxCell> list = activityNetwork.getActivityNodes();

        EV = 0;
        for (mxCell cell : list) {
            ActivityOnNode node = (ActivityOnNode) cell.getValue();

            EV += node.getEV();
        }
    }

    public float getBAC() {
        return BAC;
    }

    public float getPV() {
        return PV;
    }

    public float getEV() {
        return EV;
    }

    public int getDate() {
        return date;
    }

    public LocalDate getCalendarDate() {
        return activityNetwork.getCalendarDate(date);
    }

    public float getPercentScheduledForCompletion() {
        return PV / BAC;
    }

    public float getPercentComplete() {
        return EV / BAC;
    }

    public float getCostVariance() {
        return EV - AC;
    }

    public float getVarianceAtCompletion() {
        return BAC - getEstimateAtCompletion();
    }

    public float getScheduleVariance() {
        return EV - PV;
    }

    public float getCostPerformanceIndex() {
        return EV / AC;
    }

    public float getSchedulePerformanceIndex() {
        return EV / PV;
    }

    public float getEstimateAtCompletion() {
        return BAC / getCostPerformanceIndex();
    }

    public float getEstimateToComplete() {
        return getEstimateAtCompletion() - AC;
    }
}
