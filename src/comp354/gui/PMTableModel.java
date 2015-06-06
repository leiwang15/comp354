package comp354.gui;

import comp354.Activity;
import comp354.ActivityList;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Arrays;
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

        for (int j = 0; j < getRowCount(); j++) {
            for (int i = 0; i < getColumnCount(); i++) {
                System.out.print(getValueAt(j, i) + "\t");
            }
            System.out.println();
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
            Activity activity = new Activity();
            activity.setActivity_id(Integer.parseInt((String) getValueAt(j, 0)));
            activity.setActivity_name((String) getValueAt(j, 1));
            activity.setDuration(Integer.parseInt((String) getValueAt(j, 2)));

            ArrayList<Integer> predecessors = new ArrayList<Integer>();
            for (String p : ((String) getValueAt(j, 3)).split(",")) {
                predecessors.add(Integer.parseInt(p.trim()));
            }
            activity.setPredecessors(predecessors);
        }

        return activityList;
    }
}
