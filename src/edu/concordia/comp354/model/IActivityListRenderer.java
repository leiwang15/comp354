package edu.concordia.comp354.model;

import com.mxgraph.view.mxGraph;

import java.util.ArrayList;

/**
 * Created by joao on 15.07.12.
 */
public interface IActivityListRenderer {

    int getRowHeight();
    int getXScale();
    int getXGap();

    ArrayList<Activity> getActivityList();

    void autoLayout(mxGraph graph);

    void setCPMData(Object[] objects, Object[] objects1);

    void activitySelected(int id);
}
