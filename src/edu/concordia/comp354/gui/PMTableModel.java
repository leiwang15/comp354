package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.Activity;
import org.apache.commons.lang3.StringUtils;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by joao on 15.06.05.
 */
public class PMTableModel extends DefaultTableModel {

	protected static final int PRED_COL = 5;
	protected static final int DURATION_COL = 2;
	protected static final int ID_COL = 0;
	protected static final int NAME_COL = 1;
	ArrayList<Activity> activityList;

	public PMTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
		activityList = new ArrayList<Activity>();
        addActivity(1);
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
			String s = (String) getValueAt(j, ID_COL);
			if (!s.equals("")) {
				return Integer.parseInt(s);
			}
		}
		return 0;
	}

	public ArrayList<Activity> getActivityList() {
//        System.out.println("getActivityList ");

		for (int j = 0; j < getRowCount(); j++) {
			if (StringUtils.isNotEmpty((String) getValueAt(j, ID_COL))) {

				Activity activity = activityList.get(j);
				activity.setActivity_id(Integer.parseInt((String) getValueAt(j, ID_COL)));
				activity.setActivity_name((String) getValueAt(j, NAME_COL));
				activity.setDuration(getValueAt(j, DURATION_COL) != null ? (Integer) getValueAt(j, DURATION_COL) : 0);

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

		return activityList;
	}

	public void addActivity(int activityID) {
		activityList.add(new Activity(activityID, "", "", 0, new ArrayList<Integer>(), 0, 0, 0));
	}

	public void deleteActivity(int activityID) {
		activityList.remove(activityID - 1);
		//todo: notify subscriber of deletion
	}
}
