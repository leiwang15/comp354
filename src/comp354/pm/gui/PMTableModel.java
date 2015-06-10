package comp354.pm.GUI;

import comp354.pm.Model.Activity;
import comp354.pm.Model.ActivityList;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by joao on 15.06.05.
 */
public class PMTableModel extends DefaultTableModel {

    public PMTableModel(Object[][] data, Object[] columnNames) {
        super(data, columnNames);
    }

    /**
     * Constructs a <code>DefaultTableModel</code> and initializes the table
     * by passing <code>data</code> and <code>columnNames</code>
     * to the <code>setDataVector</code> method.
     *
     * @param data        the data of the table, a <code>Vector</code>
     *                    of <code>Vector</code>s of <code>Object</code>
     *                    values
     * @param columnNames <code>vector</code> containing the names
     *                    of the new columns
     * @see #getDataVector
     * @see #setDataVector
     */
    public PMTableModel(Vector data, Vector columnNames) {
        super(data, columnNames);
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return column != 0;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        for (int j = 0; j < getRowCount(); j++) {
            for (int i = 0; i < getColumnCount(); i++) {
                sb.append(getValueAt(j, i)).append("\t");
            }
            sb.append("\n");
        }
        return "";
    }

    public int getLastID(int currentRow) {
        for (int j = currentRow; j >= 0; j--) {
            String s = (String) getValueAt(j, 0);
            if (!s.equals("")) {
                return Integer.parseInt(s);
            }
        }
        return -1;
    }

    public ActivityList fillActivityList() {
        ActivityList activityList = new ActivityList();

        for (int j = 0; j < getRowCount(); j++) {
            if (StringUtils.isNotEmpty((String) getValueAt(j, 0))) {
                Activity activity = new Activity(1, "", "", 0, new ArrayList<Integer>());
                activity.setActivity_id(Integer.parseInt((String) getValueAt(j, 0)));
                activity.setActivity_name((String) getValueAt(j, 1));
                activity.setDuration(Integer.parseInt(StringUtils.defaultString((String) getValueAt(j, 2), "0")));

                ArrayList<Integer> predecessors = new ArrayList<Integer>();

                String predStr = (String) getValueAt(j, 3);
                if (StringUtils.isNotEmpty(predStr)) {
                    for (String p : predStr.split(",")) {
                        predecessors.add(Integer.parseInt(StringUtils.defaultString(p.trim(), "0")));
                    }
                    activity.setPredecessors(predecessors);
                }

                activityList.getActivities().add(activity);
            }
        }

        return activityList;
    }
}
