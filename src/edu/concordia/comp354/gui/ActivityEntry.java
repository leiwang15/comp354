package edu.concordia.comp354.gui;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.IActivityEntryRenderer;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


/**
 * Created by joao on 15.06.05.
 */
public abstract class ActivityEntry extends BaseTab implements IActivityEntryRenderer, ActionListener, ItemListener {
	public static final String DATE_FORMAT = "d MMM, yyyy";

	public static final int X_SCALE = 20;
	protected static final int X_GAP = 0;

	protected JTable table;
	protected JPanel charts;
	private JScrollPane tablePane;
	private JScrollPane chartPane;
	public PMTableModel dtm;
	protected String[] columnNames;

	protected Object[][] tableRows;
	protected mxGraphComponent graphComponent;
	private int previousID = -1;
	protected JSplitPane splitPane;

	public ActivityEntry(MainRenderer mainRenderer) {
		super(mainRenderer);
		createUIComponents();

		graphComponent = new mxGraphComponent(new mxGraph());

		previousID = -1;

		getMainRenderer().setActivityEntry(this);
	}

	public PMTable getTable() {
		return (PMTable) table;
	}

	private void createUIComponents() {

		createEntryColumns();

		table.setGridColor(new Color(211, 211, 211));
		table.setPreferredScrollableViewportSize(new Dimension(450, 400));
		table.setEnabled(true);
		table.setDropMode(DropMode.USE_SELECTION);
		table.setForeground(new Color(0, 0, 0));
		table.setBackground(new Color(255, 255, 255));
		table.setIntercellSpacing(new Dimension(1, 1));
		table.setSelectionBackground(new Color(202, 202, 202));
		table.setSelectionForeground(new Color(0, 0, 0));
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		table.setUpdateSelectionOnSort(true);

		tablePane = new JScrollPane(table);

		charts = new JPanel(new BorderLayout());

		charts.setForeground(new Color(0, 0, 0));
		charts.setBackground(new Color(238, 238, 238));
		charts.setEnabled(true);


		chartPane = new JScrollPane(charts);
		chartPane.setEnabled(true);
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane, chartPane);
		splitPane.setResizeWeight(0);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(330);

//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		splitPane.setMinimumSize(minimumSize);
		splitPane.setMaximumSize(new Dimension(-1, -1));

		setLayout(new BorderLayout());
		add(splitPane);


		final InputVerifier iv = new InputVerifier() {

			@Override
			public boolean verify(JComponent input) {
				JTextField field = (JTextField) input;
				if (StringUtils.isNotEmpty(field.getText())) {
					String tmp = field.getText().replaceAll("[,;]", " ");
					if (StringUtils.isNumericSpace(tmp)) {
						for (String s : tmp.split(" ")) {
							int pred = Integer.parseInt(s);
							if (pred < 1 || pred > ((PMTable) table).getMaxPredID()) {
								return false;
							}
						}
					}
				}
				return true;
			}

			@Override
			public boolean shouldYieldFocus(JComponent input) {
				boolean valid = verify(input);
				if (!valid) {
					String errorMsg = "Invalid entry: ID must be between 1 and " + ((PMTable) table).getMaxPredID();
					JOptionPane.showMessageDialog(ActivityEntry.this, errorMsg);
				}
				return valid;
			}

		};
		DefaultCellEditor editor = new DefaultCellEditor(new JTextField()) {
			{
				getComponent().setInputVerifier(iv);
			}

			@Override
			public boolean stopCellEditing() {
				if (!iv.shouldYieldFocus(getComponent())) return false;
				return super.stopCellEditing();
			}

			@Override
			public JTextField getComponent() {
				return (JTextField) super.getComponent();
			}

		};
	}

	protected abstract void createEntryColumns();

	public void autoResizeColumns() {
		table.getTableHeader().setResizingAllowed(true);
		for (int i = 0; i < table.getColumnCount();i++) {
			table.getColumn(table.getColumnName(i)).sizeWidthToFit();
		}
	}

	/*
		Code actions for menus here
	 */
	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("Delete")) {
			ActivityEntry.this.deleteActivity();
		}

		repaint();
	}

	public void setCPMData(Object[] nonCriticals, Object[] criticals) {
		getProjectManager().getActivityNetwork().ganttGraph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", nonCriticals);
		getProjectManager().getActivityNetwork().ganttGraph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticals);
	}

	public void activitySelected(int id) {
		if (getCurrentProject() != null) {
			if (previousID != -1 && previousID < getCurrentProject().getActivities().size()) {
				getProjectManager().losingDetailFocus(previousID);
			}

			if (id < getCurrentProject().getActivities().size()) {
				getProjectManager().gainingDetailFocus(id);
			} else {
				clearActivityDetails();
			}
		}
		previousID = id;
	}

	@Override
	public int getProjectID() {
		return getCurrentProject().getProject_id();
	}

	@Override
	public void filterByUser(String userName) {
		table.repaint();
	}

	@Override
	public boolean isActiveActivity(int id) {
		return getProjectManager().isActiveActivity(id);
	}

	@Override
	public void clearActivityDetails() {
		getProjectManager().getActivityDetailRenderer().setUIDetailsFromActivity(new Activity());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		String newline = "\n";
		JMenuItem source = (JMenuItem) (e.getSource());
		String s = "Item event detected."
				+ newline
				+ "    Event source: " + source.getText()
				+ " (an instance of " + getClassName(source) + ")"
				+ newline
				+ "    New state: "
				+ ((e.getStateChange() == ItemEvent.SELECTED) ?
				"selected" : "unselected");
		System.out.println(s + newline);
	}

	// Returns just the class name -- no package info.
	protected String getClassName(Object o) {
		String classString = o.getClass().getName();
		int dotIndex = classString.lastIndexOf(".");
		return classString.substring(dotIndex + 1);
	}

	@Override
	public int getRowHeight() {
		return table.getRowHeight();
	}

	@Override
	public int getXScale() {
		return X_SCALE;
	}

	@Override
	public int getXGap() {
		return X_GAP;
	}

	@Override
	public void fillActivityList() {
		dtm.readForm();
	}

	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		table.setEnabled(enabled);
	}

	class PopUpDeleteActivity extends JPopupMenu {
		JMenuItem anItem;

		public PopUpDeleteActivity() {
			anItem = new JMenuItem("Delete");
			add(anItem);
			anItem.addActionListener(ActivityEntry.this);
		}
	}

	public class PopClickListener extends MouseAdapter {
		public void mousePressed(MouseEvent e) {
			if (e.isPopupTrigger()) {
				doPop(e);
			}
		}

		public void mouseReleased(MouseEvent e) {
			if (e.isPopupTrigger())
				doPop(e);
		}

		private void doPop(MouseEvent e) {
			PopUpDeleteActivity menu = new PopUpDeleteActivity();

			int row = table.rowAtPoint(e.getPoint());
			if (!table.isRowSelected(row)) {
				table.setRowSelectionInterval(row, row);
			}

			menu.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public boolean isActivitySelected() {
		int row = getSelectedActivityRow();

		return 0 <= row && row <= getCurrentProject().getActivities().size();
	}

	public int getSelectedActivityRow() {
		return table.getSelectedRow();
	}

	public abstract void deleteActivity();
//
//	@Override
//	public void setVisible(boolean flag) {
//		super.setVisible(flag);
//
////		table.setVisible(flag);
//		charts.setVisible(flag);
////		tablePane.setVisible(flag);
////		chartPane.setVisible(flag);
//		graphComponent.setVisible(flag);
////		splitPane.setVisible(flag);
//	}
}
