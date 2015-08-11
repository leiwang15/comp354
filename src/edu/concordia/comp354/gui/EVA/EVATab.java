package edu.concordia.comp354.gui.EVA;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import edu.concordia.comp354.gui.MainRenderer;
import edu.concordia.comp354.gui.PMTable;
import edu.concordia.comp354.model.EVA.EVARenderer;
import edu.concordia.comp354.model.EVA.EarnedValueAnalysis;
import edu.concordia.comp354.model.EVA.EarnedValuePoint;
import edu.concordia.comp354.model.ProjectManager;
import net.objectlab.kit.datecalc.common.DateCalculator;
import org.jfree.chart.ChartPanel;

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
    private ChartPanel chartPanel;

    public EVATab(MainRenderer mainRenderer) {
        super(mainRenderer, ProjectManager.EVA);
    }

    protected void initializeTab() {

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
                if (e != null && e.getSource() != null && ((JComboBox<String>) e.getSource()).getSelectedItem() != null) {
                    getProjectManager().evaDateSelected(((JComboBox<String>) e.getSource()).getSelectedItem().toString());
                }
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
            JComboBox<String> selector = getMainRenderer.evaDateSelector;

            String selectedItem = (String) selector.getSelectedItem();
            selector.removeAllItems();
            LocalDate startDate = getCurrentProject().getStart_date();

            DateCalculator<LocalDate> dateCalculator = ProjectManager.getDateCalculator();
            for (EarnedValuePoint point : getCurrentProject().getEvaPoints()) {
                dateCalculator.setStartDate(startDate);
                dateCalculator.moveByBusinessDays(point.getDate());
                selector.addItem(dateCalculator.getCurrentBusinessDate().format(DATE_FORMAT));
            }
            if (selectedItem != null) {
                selector.setSelectedItem(selectedItem);
            } else {
                selector.setSelectedIndex(0);
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

            DateCalculator<LocalDate> dateCalculator = ProjectManager.getDateCalculator();

            if (getCurrentProject() != null) {
                LocalDate start_date = getCurrentProject().getStart_date();
                List<EarnedValuePoint> points = getCurrentProject().getEvaPoints();
                int i;
                for (i = 0; i < points.size(); i++) {
                    EarnedValuePoint point = points.get(i);

                    dateCalculator.setStartDate(start_date);
                    dateCalculator.moveByBusinessDays(point.getDate());

                    tableRows[i] = new Object[]{
                            point.getDBID(),
                            dateCalculator.getCurrentBusinessDate().format(DATE_FORMAT),
                            MainRenderer.DOLLAR_FORMAT.format(point.getPlannedValue()),
                            MainRenderer.DOLLAR_FORMAT.format(point.getEarnedValue()),
                            MainRenderer.DOLLAR_FORMAT.format(point.getActualCost())
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

        mainRenderer.pvFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getPV()));
        mainRenderer.evFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getEV()));
        mainRenderer.acFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getAC()));
        mainRenderer.bacFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getBAC()));

        mainRenderer.svFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getScheduleVariance()));
        mainRenderer.cvFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getCostVariance()));
        mainRenderer.cpiFld.setText(MainRenderer.DECIMAL_FORMAT.format(eva.getCostPerformanceIndex()));
        mainRenderer.spiFld.setText(MainRenderer.DECIMAL_FORMAT.format(eva.getSchedulePerformanceIndex()));
        mainRenderer.eacFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getEstimateAtCompletion()));
        mainRenderer.etcFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getEstimateToComplete()));
        mainRenderer.vacFld.setText(MainRenderer.DOLLAR_FORMAT.format(eva.getVarianceAtCompletion()));
        mainRenderer.completedFld.setText(MainRenderer.PERCENT_FORMAT.format(eva.getPercentComplete()));
    }

    @Override
    public void selectEVADate(String dateStr) {
        getMainRenderer().selectEVADate(dateStr);
    }

    @Override
    public void setActivityList() {
        fillTable(true);
    }

    @Override
    public void autoLayout(mxGraph graph) {

        List<EarnedValuePoint> points = getCurrentProject().getEvaPoints();

        Charts chart = new Charts("Earned Value Analysis", DATE, "Value ($)", points, getCurrentProject().getStart_date());

        if (chartPanel == null) {
            chartPanel = new ChartPanel(chart.chart);
            chartPanel.setPreferredSize(new Dimension(500, 270));
            charts.add(chartPanel, BorderLayout.CENTER);

        } else {
            chartPanel.setChart(chart.chart);
        }
        chartPanel.setVisible(true);

        charts.revalidate();
    }
}
