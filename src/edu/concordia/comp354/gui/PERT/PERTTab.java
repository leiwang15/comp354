package edu.concordia.comp354.gui.PERT;

import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import edu.concordia.comp354.gui.MainRenderer;
import edu.concordia.comp354.gui.PMTable;
import edu.concordia.comp354.gui.PMTableModel;
import edu.concordia.comp354.gui.editors.PredecessorEditor;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.IActivityEntryRenderer;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by joao on 15.07.25.
 */
public class PERTTab extends ActivityEntry implements IActivityEntryRenderer {

    private static final String OPTIMISTIC = "Optimistic";
    private static final String NORMAL = "Normal";
    private static final String PESSIMISTIC = "Pessimistic";
    private static final String EXPECTED = "Expected (t)";
    private static final String STDEV = "Stdev (s)";
    public static DecimalFormat df = new DecimalFormat("##.##");

    public PERTTab(MainRenderer mainRenderer) {
        super(mainRenderer);
    }

    protected void initializeTab() {

        setName("PERT");

        JPanel parentContainer = new JPanel();
        getMainRenderer().tabbedPane.addTab(getName(), null, this, null);

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);
    }

    protected void createEntryColumns() {
        tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID,
                PMTable.NAME,
                OPTIMISTIC,
                NORMAL,
                PESSIMISTIC,
                PMTable.START,
                PMTable.FINISH,
                PMTable.PREDECESSORS,
                EXPECTED,
                STDEV};
        dtm = new PMTableModelPERT(tableRows, columnNames, getProjectManager());

        dtm.clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

//        table = new PMTable(dtm, scm, this);
        table = new PERTTable(dtm, scm, this);

        table.setCellSelectionEnabled(true);
        table.addMouseListener(new PopClickListener());

        table.createDefaultColumnsFromModel();

        table.getColumn(OPTIMISTIC).setCellEditor(new JXTable.NumberEditor());
        table.getColumn(NORMAL).setCellEditor(new JXTable.NumberEditor());
        table.getColumn(PESSIMISTIC).setCellEditor(new JXTable.NumberEditor());
        table.getColumn(PMTable.START).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        table.getColumn(PMTable.FINISH).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        table.getColumn(PMTable.PREDECESSORS).setCellEditor(new PredecessorEditor(this));

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(10, 38));

        table.getColumn(PMTable.ID).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(OPTIMISTIC).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(NORMAL).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        table.getColumn(PESSIMISTIC).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
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
                            activity.getOptimistic() != 0 ? activity.getOptimistic() : null,
                            activity.getDuration() != 0 ? activity.getDuration() : null,
                            activity.getPessimistic() != 0 ? activity.getPessimistic() : null,
                            "",
                            "",
                            activity.getPredecessors() != null ? activity.getPredecessors().toString().replaceAll("\\[|\\]", "") : "",
                            df.format(activity.getExpectedDate()),
                            df.format(activity.getStdev())
                    };
                }

                for (; i < PMTable.MAX_TABLE_SIZE; i++) {
                    tableRows[i] = new String[]{""};
                }

                dtm.setDataVector(tableRows, columnNames);

                getProjectManager().getActivityNetwork().createPERTChart();
            }
        }
    }

    @Override
    public void gainFocus() {

        getMainRenderer().activityPanel.setVisible(true);
        getMainRenderer().evaPanel.setVisible(false);
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
        getProjectManager().getActivityNetwork().createPERTChart();
    }

    public void autoLayout(mxGraph graph) {
        charts.remove(graphComponent);

        graphComponent = new PERTPanel(this, graph);

        charts.add(graphComponent, BorderLayout.CENTER);

        mxHierarchicalLayout layout = new mxHierarchicalLayout(graph, SwingConstants.WEST);
        layout.setIntraCellSpacing(80);
        layout.setInterRankCellSpacing(100);
        layout.execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
        charts.revalidate();
    }
}
