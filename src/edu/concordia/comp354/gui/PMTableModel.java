package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.ProjectManager;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by joao on 15.06.05.
 */
public class PMTableModel extends DefaultTableModel {

    protected static final int PRED_COL = 5;
    protected static final int DURATION_COL = 2;
    protected static final int ID_COL = 0;
    protected static final int NAME_COL = 1;
    private final Object[] columnNames;
	final ProjectManager projectManager;

    public PMTableModel(Object[][] data, Object[] columnNames, ProjectManager projectManager) {
        super(data, columnNames);

        this.columnNames = columnNames;
		this.projectManager = projectManager;
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
    public PMTableModel(Vector data, Vector columnNames, ProjectManager projectManager) {
        super(data, columnNames);
        this.columnNames = columnNames.toArray();

		this.projectManager = projectManager;
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
            String s = (String) getValueAt(j, ID_COL);
            if (!s.equals("")) {
                return Integer.parseInt(s);
            }
        }
        return 0;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 2:
                return Integer.class;
            default:
                return String.class;
        }
    }

    public void fillActivityList() {

		List<Activity> activityList = projectManager.getCurrentProject().getActivities();

		for (int j = 0; j < activityList.size(); j++) {
//        for (int j = 0; j < getRowCount(); j++) {
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

	public void deleteActivity(int activityID) {
		projectManager.getCurrentProject().deleteActivity(activityID);
    }

    public void clear() {

//		setNumRows(1024);

        Object[][] tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
//        tableRows[0] = new String[]{"1"}; //  may be incompatible with maxPredID at startup
        tableRows[0] = new String[]{""};
        for (int i = 1; i < PMTable.MAX_TABLE_SIZE; i++) {
            tableRows[i] = new String[]{""};
        }

        setDataVector(tableRows, columnNames);
    }
}
