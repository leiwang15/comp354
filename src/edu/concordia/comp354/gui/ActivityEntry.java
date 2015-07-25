package edu.concordia.comp354.gui;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import de.jollyday.Holiday;
import de.jollyday.HolidayManager;
import de.jollyday.ManagerParameters;
import edu.concordia.comp354.gui.editors.IntegerEditor;
import edu.concordia.comp354.gui.editors.PredecessorEditor;
import edu.concordia.comp354.model.Activity;
import edu.concordia.comp354.model.IActivityEntryRenderer;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.common.DefaultHolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayCalendar;
import net.objectlab.kit.datecalc.common.HolidayHandlerType;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
import java.util.List;


/**
 * Created by joao on 15.06.05.
 */
public class ActivityEntry extends BaseTab implements IActivityEntryRenderer, ActionListener, ItemListener {
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    protected static final int X_SCALE = 20;
    protected static final int X_GAP = 0;

//    protected JPanel panel;
    private JTable activitiesTable;
    private JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    public PMTableModel dtm;
    String[] columnNames;

    Object[][] tableRows;
    mxGraphComponent graphComponent;
    private int previousID = -1;

    public ActivityEntry(MainWindow mainWindow) {
        super(mainWindow);
        getParentWindow().setActivityEntry(this);

        createUIComponents();

        graphComponent = new mxGraphComponent(new mxGraph());

        previousID = -1;
    }

    /*
        Set activities in grid
    */
    public void setActivities(boolean update) {
        dtm.clear();

        if (update) {

            if (getCurrentProject() != null) {
                List<Activity> activities = getCurrentProject().getActivities();
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

                getProjectManager().getActivityList().createGraph();
            }
        }
    }

    public PMTable getActivitiesTable() {
        return (PMTable) activitiesTable;
    }

    private void createUIComponents() {

        tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID, PMTable.NAME, PMTable.DURATION, PMTable.START, PMTable.FINISH, PMTable.PREDECESSORS};
        dtm = new PMTableModelGantt(tableRows, columnNames, getProjectManager());

        dtm.clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

        activitiesTable = new PMTable(dtm, scm, this);
        activitiesTable.setCellSelectionEnabled(true);
        activitiesTable.addMouseListener(new PopClickListener());

        activitiesTable.createDefaultColumnsFromModel();

        activitiesTable.getColumn(PMTable.DURATION).setCellEditor(new IntegerEditor(1, PMTable.MAX_TABLE_SIZE, this));
        activitiesTable.getColumn(PMTable.START).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.FINISH).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.PREDECESSORS).setCellEditor(new PredecessorEditor(this));

        JTableHeader header = activitiesTable.getTableHeader();
        header.setPreferredSize(new Dimension(10, 38));

        activitiesTable.getColumn(PMTable.ID).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.DURATION).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.START).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.FINISH).setHeaderRenderer(new DefaultTableCellHeaderRenderer());
        activitiesTable.getColumn(PMTable.PREDECESSORS).setHeaderRenderer(new DefaultTableCellHeaderRenderer());

        activitiesTable.getColumn(PMTable.ID).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.DURATION).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.START).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.FINISH).sizeWidthToFit();
        activitiesTable.getColumn(PMTable.PREDECESSORS).sizeWidthToFit();

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

        activitiesTable.getColumn(PMTable.START).setCellRenderer(tableCellRenderer);
        activitiesTable.getColumn(PMTable.FINISH).setCellRenderer(tableCellRenderer);

        activitiesTable.setGridColor(new Color(211, 211, 211));
        activitiesTable.setPreferredScrollableViewportSize(new Dimension(450, 400));
        activitiesTable.setEnabled(true);
        activitiesTable.setDropMode(DropMode.USE_SELECTION);
        activitiesTable.setForeground(new Color(0, 0, 0));
        activitiesTable.setBackground(new Color(255, 255, 255));
        activitiesTable.setIntercellSpacing(new Dimension(1, 1));
        activitiesTable.setSelectionBackground(new Color(202, 202, 202));
        activitiesTable.setSelectionForeground(new Color(0, 0, 0));
        activitiesTable.setShowHorizontalLines(true);
        activitiesTable.setShowVerticalLines(true);
        activitiesTable.setUpdateSelectionOnSort(true);

        tablePane = new JScrollPane(activitiesTable);

        charts = new JPanel(new BorderLayout());

        charts.setForeground(new Color(0, 0, 0));
        charts.setBackground(new Color(238, 238, 238));
        charts.setEnabled(true);


        chartPane = new JScrollPane(charts);
        chartPane.setEnabled(true);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tablePane, chartPane);
        splitPane.setResizeWeight(0);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(330);

//Provide minimum sizes for the two components in the split pane
        Dimension minimumSize = new Dimension(100, 50);
        splitPane.setMinimumSize(minimumSize);
        splitPane.setMaximumSize(new Dimension(-1, -1));

//        panel = new JPanel(new BorderLayout(), false);
//        panel.setSize(new Dimension(640, 480));
//        panel.add(splitPane);
        setLayout(new BorderLayout());
//        setSize(new Dimension(640, 480));
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
                            if (pred < 1 || pred > ((PMTable) activitiesTable).getMaxPredID()) {
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
                    String errorMsg = "Invalid entry: ID must be between 1 and " + ((PMTable) activitiesTable).getMaxPredID();
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

    public void autoLayout(mxGraph graph) {
        charts.remove(graphComponent);

        graphComponent = new ChartPanel(graph);
//        graphComponent = new mxGraphComponent(graph);

        charts.add(graphComponent, BorderLayout.CENTER);

//        new mxHierarchicalLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
//        new mxParallelEdgeLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
        charts.revalidate();
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
        getProjectManager().getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", nonCriticals);
        getProjectManager().getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticals);
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
    public void clear() {
        setActivities(true);
        repaint();
    }

    @Override
    public int getProjectID() {
        return getCurrentProject().getProject_id();
    }

    @Override
    public void filterByUser(String userName) {
        activitiesTable.repaint();
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
        return activitiesTable.getRowHeight();
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
    public void setActivityList() {
        setActivities(true);
    }

    @Override
    public void fillActivityList() {
        dtm.fillActivityList();
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        activitiesTable.setEnabled(enabled);
//        panel.setEnabled(enabled);
    }

    class ChartPanel extends mxGraphComponent {
        public ChartPanel(mxGraph graph) {
            super(graph);
        }

        @Override
        protected void paintBackground(Graphics g) {
            super.paintBackground(g);
            Graphics2D g2 = (Graphics2D) g;

            String country = Locale.getDefault().getCountry();

            HolidayManager.setManagerCachingEnabled(true);
            HolidayManager manager = HolidayManager.getInstance(ManagerParameters.create("Canada"));


// create or get the Holidays
            final Set<Holiday> holidays = manager.getHolidays(2015);

// fill dates into set of LocalDate
            Set<LocalDate> holidayDates = new HashSet<LocalDate>();
            for (Holiday h : holidays) {
                holidayDates.add(h.getDate());
            }

            DateTimeFormatter localFormatter = DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.MEDIUM).withLocale(Locale.getDefault());

// create the HolidayCalendar ASSUMING that the set covers 2015!
            final HolidayCalendar<LocalDate> calendar = new DefaultHolidayCalendar<LocalDate>
                    (holidayDates, LocalDate.parse("2015-01-01"), LocalDate.parse("2025-12-31"));

// register the holidays, any calculator with name "Canada"
// asked from now on will receive an IMMUTABLE reference to this calendar
            LocalDateKitCalculatorsFactory.getDefaultInstance().registerHolidays("Canada", calendar);

// ask for a LocalDate calculator for "Canada"
// (even if a new set of holidays is registered, this one calculator is not affected
            DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.getDefaultInstance()
                    .getDateCalculator("Canada", HolidayHandlerType.FORWARD);


            LocalDate date = getCurrentProject().getStart_date();

            dateCalculator.setStartDate(date);

            if (dateCalculator.getCurrentBusinessDate().equals(date)) {
                //  set to first work day of  week
                while (dateCalculator.isCurrentDateNonWorking()) {
                    dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(-1));
                }
                dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
            }

            int x = 0;
            int y = 0;
            int zoom = X_SCALE;

            String[] days = "S M T W T F S".split(" ");

            int numOfVisibleDays = getWidth() / X_SCALE;

            final int DAYS_PER_WEEK = 7;

            //  find first Sunday before start date

            LocalDate curDate = dateCalculator.getStartDate();
            while (curDate.getDayOfWeek() != DayOfWeek.SUNDAY) {
                curDate = curDate.plusDays(-1);
            }
            Color oldColor = g.getColor();
            g.setColor(new Color(245, 245, 245));
            g2.fillRect(0, y * zoom - 2 + 1, getWidth(), zoom * 2 - 5);
            g.setColor(oldColor);

            int numOfVisibleWeeks = numOfVisibleDays / DAYS_PER_WEEK + 1;
            for (int i = 0; i < numOfVisibleWeeks; i++) {
                g2.drawString(curDate.format(DateTimeFormatter.ofPattern("dd MMM yy")), i * X_SCALE * DAYS_PER_WEEK + 6, y * zoom + zoom / 2 + 1);
                curDate = curDate.plusDays(DAYS_PER_WEEK);
            }


            oldColor = g.getColor();
            g.setColor(new Color(245, 245, 245));
            g2.fillRect(0, y * zoom + zoom / 2 + 1, X_SCALE, getHeight());
            for (int i = 1; i < numOfVisibleWeeks; i++) {
                g2.fillRect(i * X_SCALE * DAYS_PER_WEEK - X_SCALE, y * zoom + zoom / 2 + 4 + 1, 2 * X_SCALE, getHeight());
                g.setColor(Color.LIGHT_GRAY);
                g2.drawLine(i * X_SCALE * DAYS_PER_WEEK, y * zoom + zoom / 2 + 4 + 1, i * X_SCALE * DAYS_PER_WEEK + 1, getHeight());
                g.setColor(new Color(245, 245, 245));
            }
            g.setColor(oldColor);

            g.setColor(Color.LIGHT_GRAY);
            g2.drawLine(x * zoom + 6, y * zoom + zoom / 2 + 3 + 1, getWidth(), y * zoom + zoom / 2 + 3 + 1);
            g.setColor(Color.GRAY);


            for (int i = 0; i < numOfVisibleDays; i++) {

                g2.drawString(days[i % days.length], i * X_SCALE + 6, y * zoom + zoom / 2 + 19 + 1);
            }
            g2.drawLine(x * zoom + 6, y * zoom + zoom / 2 + 3 + 20 + 1, getWidth(), y * zoom + zoom / 2 + 3 + 20 + 1);
        }

        @Override
        public void paint(Graphics g) {

            super.paint(g);
        }
    }

    class PopUpDeleteActivity extends JPopupMenu {
        JMenuItem anItem;

        public PopUpDeleteActivity() {
            anItem = new JMenuItem("Delete");
            add(anItem);
            anItem.addActionListener(ActivityEntry.this);
        }
    }

    class PopClickListener extends MouseAdapter {
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

            int row = activitiesTable.rowAtPoint(e.getPoint());
            if (!activitiesTable.isRowSelected(row)) {
                activitiesTable.setRowSelectionInterval(row, row);
            }

            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    public void deleteActivity() {
        getProjectManager().deleteActivity(getSelectedActivityRow());

        ((PMTableModel) activitiesTable.getModel()).removeRow(getSelectedActivityRow());
        setActivities(true);
        getProjectManager().getActivityList().createGraph();
    }

    public boolean isActivitySelected() {
        int row = getSelectedActivityRow();

        return 0 <= row && row <= getCurrentProject().getActivities().size();
    }

    public int getSelectedActivityRow() {
        return activitiesTable.getSelectedRow();
    }
}
