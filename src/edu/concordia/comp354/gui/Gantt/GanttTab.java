package edu.concordia.comp354.gui.Gantt;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import edu.concordia.comp354.gui.MainRenderer;
import edu.concordia.comp354.gui.PMTable;
import edu.concordia.comp354.gui.PMTableModel;
import edu.concordia.comp354.gui.editors.IntegerEditor;
import edu.concordia.comp354.gui.editors.PredecessorEditor;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.EVA.EarnedValueAnalysis;
import edu.concordia.comp354.model.ProjectManager;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class GanttTab extends ActivityEntry {

    public GanttTab(MainRenderer mainRenderer) {
        super(mainRenderer, ProjectManager.GANTT);
    }

    protected void initializeTab() {

        JPanel parentContainer = new JPanel();
        getMainRenderer().tabbedPane.addTab(getName(), null, this, null);

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);
    }

    @Override
    public void gainFocus() {

        getMainRenderer().activityPanel.setVisible(true);
        getMainRenderer().evaPanel.setVisible(false);
    }

    protected void createEntryColumns() {
        tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID, PMTable.NAME, PMTable.DURATION, PMTable.START, PMTable.FINISH, PMTable.PREDECESSORS};
        dtm = new PMTableModelGantt(tableRows, columnNames, getProjectManager());

        dtm.clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

        table = new PMTable(dtm, scm, this);
        table.setCellSelectionEnabled(true);
        table.addMouseListener(new PopClickListener());

        table.createDefaultColumnsFromModel();

        table.getColumn(PMTable.DURATION).setCellEditor(new IntegerEditor(1, PMTable.MAX_TABLE_SIZE, this));
        table.getColumn(PMTable.START).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        table.getColumn(PMTable.FINISH).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        table.getColumn(PMTable.PREDECESSORS).setCellEditor(new PredecessorEditor(this));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(10, 38));

        table.getColumn(PMTable.ID).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(PMTable.DURATION).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(PMTable.START).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(PMTable.FINISH).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(PMTable.PREDECESSORS).setHeaderRenderer(new DefaultTableCellHeaderRenderer());

        autoResizeColumns();

        TableCellRenderer tableCellRenderer = new DefaultTableCellRenderer() {

            SimpleDateFormat f = new SimpleDateFormat(DATE_FORMAT);

            public Component getTableCellRendererComponent(JTable table,
                                                           Object value, boolean isSelected, boolean hasFocus,
                                                           int row, int column) {
                if (value instanceof Date) {
                    value = f.format(value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
            }
        };

        table.getColumn(PMTable.START).setCellRenderer(tableCellRenderer);
        table.getColumn(PMTable.FINISH).setCellRenderer(tableCellRenderer);
    }

//    public void autoResizeColumns() {
//        table.getColumn(PMTable.ID).sizeWidthToFit();
//        table.getColumn(PMTable.DURATION).sizeWidthToFit();
//        table.getColumn(PMTable.START).sizeWidthToFit();
//        table.getColumn(PMTable.FINISH).sizeWidthToFit();
//        table.getColumn(PMTable.PREDECESSORS).sizeWidthToFit();
//    }

    /*
            Set activities in grid
        */
    public void setActivities(boolean update) {
        dtm.clear();

        if (update) {

            if (getCurrentProject() != null) {
                java.util.List<Activity> activities = getCurrentProject().getActivities();
                int i;
                for (i = 0; i < activities.size(); i++) {
                    Activity activity = activities.get(i);

                    tableRows[i] = new Object[]{

                            Integer.toString(activity.getActivity_id()),
                            activity.getActivity_name(),
                            activity.getDuration() != 0 ? activity.getDuration() : null,
                            "",
                            "",
                            activity.getPredecessors() != null ? activity.getPredecessors().toString().replaceAll("\\[|\\]", "") : ""
                    };
                }

                for (; i < PMTable.MAX_TABLE_SIZE; i++) {
                    tableRows[i] = new String[]{""};
                }

                dtm.setDataVector(tableRows, columnNames);

                getProjectManager().getActivityNetwork().createAONNetwork();
            }
        }
    }

    @Override
    public void clear() {
        setActivities(true);
        repaint();
    }

    @Override
    public void setActivityList() {
        setActivities(true);
    }

    public void deleteActivity() {
        getProjectManager().deleteActivity(getSelectedActivityRow());

        ((PMTableModel) table.getModel()).removeRow(getSelectedActivityRow());
        setActivities(true);
        getProjectManager().getActivityNetwork().createAONNetwork();
    }

    public void autoLayout(mxGraph graph) {
//        charts.removeAll();
        charts.remove(graphComponent);

        graphComponent = new GanttPanel(this,graph);

        charts.add(graphComponent, BorderLayout.CENTER);

        charts.revalidate();
    }
}
