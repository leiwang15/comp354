package edu.concordia.comp354.gui.EVA;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import edu.concordia.comp354.gui.MainRenderer;
import edu.concordia.comp354.gui.PMTable;
import edu.concordia.comp354.model.EVA.EVARenderer;
import edu.concordia.comp354.model.EVA.EarnedValueAnalysis;
import edu.concordia.comp354.model.EVA.EarnedValuePoint;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EVATab extends ActivityEntry implements EVARenderer {

	protected static final String DATE = "Date";
	protected static final String PV = "Planned Value";
	protected static final String EV = "Earned Value";
	protected static final String AC = "Actual Cost";
	public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern(ActivityEntry.DATE_FORMAT);
	private EVAPanel evaPanel;

	public EVATab(MainRenderer mainRenderer) {
		super(mainRenderer);
	}

	protected void initializeTab() {

		setName("EVA");

		JPanel parentContainer = new JPanel();
		getMainRenderer().tabbedPane.addTab(getName(), null, this, null);

		parentContainer.setLayout(new BorderLayout());

		Dimension dimension = new Dimension(600, 600);
		parentContainer.setMaximumSize(dimension);
		parentContainer.setMinimumSize(dimension);
		parentContainer.setPreferredSize(dimension);

		splitPane.setDividerLocation(430);

		getMainRenderer().evaDateSelector.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getProjectManager().evaDateSelected(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
			}
		});

		getProjectManager().setEvaRenderer(this);
	}

	@Override
	public void gainFocus() {
		MainRenderer getMainRenderer = getMainRenderer();
		getMainRenderer.setDetailEnabled(false);
		getProjectManager().EVASelected();

		if (getCurrentProject() != null) {
//			getMainRenderer.evaDateSelector.removeAll();
			LocalDate startDate = getCurrentProject().getStart_date();
			for (EarnedValuePoint point : getCurrentProject().getEVAPoints()) {
				getMainRenderer.evaDateSelector.addItem(startDate.plusDays(point.getDate()).format(DATE_FORMAT));
			}
		}

		getMainRenderer.activityPanel.setVisible(false);
		getMainRenderer.evaPanel.setVisible(true);

	}

	protected void createEntryColumns() {
		tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
		columnNames = new String[]{PMTable.ID, DATE, PV, EV, AC};
		dtm = new PMTableModelEVA(tableRows, columnNames, getProjectManager());

		dtm.clear();

		DefaultTableColumnModel scm = new DefaultTableColumnModel();

		table = new EVATable(dtm, scm, this);
//        table = new JTable(dtm, scm);
		table.setCellSelectionEnabled(true);
		table.addMouseListener(new PopClickListener());

		table.createDefaultColumnsFromModel();

		JTableHeader header = table.getTableHeader();
		header.setPreferredSize(new Dimension(100, 38));

//        table.getColumn(PMTable.ID).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
//        table.getColumn(DATE).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
//        table.getColumn(PV).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
//        table.getColumn(EV).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
//        table.getColumn(AC).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
//        table.getColumn(DATE).setPreferredWidth(200);

//        table.getColumn(AC).setCellEditor(new IntegerEditor(1, PMTable.MAX_TABLE_SIZE, this));

		DefaultTableCellRenderer rightRenderer = new DefaultTableCellRenderer();
		rightRenderer.setHorizontalAlignment(JLabel.RIGHT);

		table.getColumn(DATE).setCellRenderer(rightRenderer);
		table.getColumn(PV).setCellRenderer(rightRenderer);
		table.getColumn(EV).setCellRenderer(rightRenderer);
		table.getColumn(AC).setCellRenderer(rightRenderer);


		TableColumn column = table.getColumn(PMTable.ID);
		column.setMinWidth(0);
		column.setMaxWidth(0);
		column.setWidth(0);
		column.setPreferredWidth(0);

		autoResizeColumns();
	}

	@Override
	public void deleteActivity() {
	}

	public void fillTable(boolean update) {
		dtm.clear();

		if (update) {
			getProjectManager().getActivityNetwork().createAONNetwork();

			if (getCurrentProject() != null) {
				LocalDate start_date = getCurrentProject().getStart_date();
				List<EarnedValuePoint> points = getCurrentProject().getEVAPoints();
				int i;
				for (i = 0; i < points.size(); i++) {
					EarnedValuePoint point = points.get(i);

					tableRows[i] = new Object[]{
							point.getDBID(),
							start_date.plusDays(point.getDate()).format(DateTimeFormatter.ofPattern(ActivityEntry.DATE_FORMAT)),
							MainRenderer.DF.format(point.getPlannedValue()),
							MainRenderer.DF.format(point.getEarnedValue()),
							MainRenderer.DF.format(point.getActualCost())
					};
				}

				for (; i < PMTable.MAX_TABLE_SIZE; i++) {
					tableRows[i] = new String[]{""};
				}

				dtm.setDataVector(tableRows, columnNames);
			}
		}
	}

	@Override
	public void clear() {
		fillTable(true);
		repaint();
	}

	@Override
	public void populateEVA(EarnedValueAnalysis eva) {
		MainRenderer mainRenderer = getMainRenderer();

		mainRenderer.pvFld.setText(Integer.toString((int)eva.getPV()));
		mainRenderer.evFld.setText(Integer.toString((int)eva.getEV()));
		mainRenderer.acFld.setText(Integer.toString((int) eva.getAC()));
		mainRenderer.bacFld.setText(Integer.toString((int)eva.getBAC()));

		mainRenderer.svFld.setText(Integer.toString((int) eva.getScheduleVariance()));
		mainRenderer.cvFld.setText(Integer.toString((int) eva.getCostVariance()));
		mainRenderer.cpiFld.setText(Integer.toString((int) eva.getCostPerformanceIndex()));
		mainRenderer.spiFld.setText(Integer.toString((int) eva.getSchedulePerformanceIndex()));
		mainRenderer.eacFld.setText(Integer.toString((int) eva.getEstimateAtCompletion()));
		mainRenderer.etcFld.setText(Integer.toString((int)eva.getEstimateToComplete()));
		mainRenderer.vacFld.setText(Integer.toString((int)eva.getVarianceAtCompletion()));
		mainRenderer.completedFld.setText(Integer.toString((int)eva.getPercentComplete()));
	}

	@Override
	public void setActivityList() {
		fillTable(true);
	}

	@Override
	public void autoLayout(mxGraph graph) {
		if (evaPanel != null) {
			charts.remove(evaPanel);
		}

		evaPanel = new EVAPanel(this, graph);

		charts.add(evaPanel, BorderLayout.CENTER);

		charts.revalidate();
	}
}
