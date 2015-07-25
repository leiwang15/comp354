package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.ProjectManager;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by joao on 15.07.25.
 */
public class PMTableModelGantt extends PMTableModel {
    protected static final int PRED_COL = 5;
    protected static final int DURATION_COL = 2;
    protected static final int NAME_COL = 1;

    public PMTableModelGantt(Object[][] data, Object[] columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    public PMTableModelGantt(Vector data, Vector columnNames, ProjectManager projectManager) {
        super(data, columnNames, projectManager);
    }

    public void fillActivityList() {

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
