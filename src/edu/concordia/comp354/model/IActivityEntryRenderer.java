package edu.concordia.comp354.model;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.model.EVA.EarnedValueAnalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joao on 15.07.12.
 */
public interface IActivityEntryRenderer {

    int getRowHeight();
    int getXScale();
    int getXGap();

    void setActivityList();
    void fillActivityList();

    void autoLayout(mxGraph graph);

    void setCPMData(Object[] objects, Object[] objects1);

    void activitySelected(int id);

    void clear();

    int getProjectID();

    void filterByUser(String userName);

    boolean isActiveActivity(int id);

    void clearActivityDetails();
}
