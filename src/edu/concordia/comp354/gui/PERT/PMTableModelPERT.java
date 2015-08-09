package edu.concordia.comp354.gui.PERT;

/**
 * Created by joao on 15.07.26.
 */

import edu.concordia.comp354.gui.PMTableModel;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.ProjectManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class PMTableModelPERT extends PMTableModel {
    protected static final int PRED_COL = 7;
    protected static final int OPTIMISTIC_COL = 2;
    protected static final int DURATION_COL = 3;
    protected static final int PESSIMISTIC_COL = 4;
    protected static final int NAME_COL = 1;

    public PMTableModelPERT(Object[][] data, Object[] columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    public PMTableModelPERT(Vector data, Vector columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    public void readForm() {

        List<Activity> activityList = projectManager.getCurrentProject().getActivities();

        for (int j = 0; j < activityList.size(); j++) {
            if (StringUtils.isNotEmpty((String) getValueAt(j, ID_COL))) {

                Activity activity = activityList.get(j);
                activity.setActivity_id(Integer.parseInt((String) getValueAt(j, ID_COL)));
                activity.setActivity_name((String) getValueAt(j, NAME_COL));

                int duration = 0;
                if (getValueAt(j, DURATION_COL) != null) {
                    if (getValueAt(j, DURATION_COL) instanceof Integer) {
                        duration = (Integer) getValueAt(j, DURATION_COL);
                    } else if (getValueAt(j, DURATION_COL) instanceof String) {
                        duration = Integer.parseInt(StringUtils.defaultIfEmpty((String) getValueAt(j, DURATION_COL), "0"));
                    }
                }
                activity.setDuration(duration);

                int optimistic = 0;
                if (getValueAt(j, OPTIMISTIC_COL) != null) {
                    if (getValueAt(j, OPTIMISTIC_COL) instanceof Integer) {
                        optimistic = (Integer) getValueAt(j, OPTIMISTIC_COL);
                    } else if (getValueAt(j, OPTIMISTIC_COL) instanceof String) {
                        optimistic = Integer.parseInt(StringUtils.defaultIfEmpty((String) getValueAt(j, OPTIMISTIC_COL), "0"));
                    }
                }
                activity.setOptimistic(optimistic);

                int pessimistic = 0;
                if (getValueAt(j, PESSIMISTIC_COL) != null) {
                    if (getValueAt(j, PESSIMISTIC_COL) instanceof Integer) {
                        pessimistic = (Integer) getValueAt(j, PESSIMISTIC_COL);
                    } else if (getValueAt(j, PESSIMISTIC_COL) instanceof String) {
                        pessimistic = Integer.parseInt(StringUtils.defaultIfEmpty((String) getValueAt(j, PESSIMISTIC_COL), "0"));
                    }
                }
                activity.setDuration(pessimistic);

                ArrayList<Integer> predecessors = new ArrayList<Integer>();

                String predStr = (String) getValueAt(j, PRED_COL);
                if (StringUtils.isNotEmpty(predStr)) {
                    for (String p : predStr.split(",")) {
                        predecessors.add(Integer.parseInt(StringUtils.defaultString(p.trim(), "0")));
                    }
                    activity.setPredecessors(predecessors);
                }
            }
        }
//        System.out.println(activityList);
    }
}
