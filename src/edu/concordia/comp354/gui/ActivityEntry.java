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
import edu.concordia.comp354.model.IActivityDetailRenderer;
import edu.concordia.comp354.model.IActivityEntryRenderer;
import edu.concordia.comp354.model.ProjectManager;
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
public class ActivityEntry extends JPanel implements IActivityEntryRenderer, ActionListener, ItemListener {
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    protected static final int X_SCALE = 20;
    protected static final int X_GAP = 0;

    protected JPanel panel1;
    private JTable activitiesTable;
    private JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    public PMTableModel dtm;
    String[] columnNames;

    Object[][] tableRows;
    mxGraphComponent graphComponent;
    private IActivityDetailRenderer detailRenderer;
    private int previousID = -1;

    private ProjectManager projectManager;

    public ActivityEntry(IActivityDetailRenderer detailRenderer, ProjectManager projectManager) {
        this.projectManager = projectManager;

        createUIComponents();

        graphComponent = new mxGraphComponent(new mxGraph());

        this.detailRenderer = detailRenderer;

        previousID = -1;
    }
//
//    /*
//        get activities from grid input
//    */
//    public ActivityList fillActivities() {
//        return this.activityList = dtm.fillActivityList();
//    }

    public ProjectManager getProjectManager() {
        return projectManager;
    }

    /*
        Set activities in grid
    */
    public void setActivities(boolean update) {
        dtm.clear();

        if (update) {

            if (projectManager.getCurrentProject() != null) {
                List<Activity> activities = projectManager.getCurrentProject().getActivities();
                int i;
                for (i = 0; i < activities.size(); i++) {
                    Activity activity = activities.get(i);

                    tableRows[i] = new Object[]{

                            Integer.toString(activity.getActivity_id()),
                            activity.getActivity_name(),
//                        activity.getDuration() != 0 ? Integer.toString(activity.getDuration()) : "",
                            activity.getDuration() != 0 ? activity.getDuration() : null,
                            "",
                            "",
                            activity.getPredecessors() != null ? activity.getPredecessors().toString().replaceAll("\\[|\\]", "") : ""
//                        activity.getPredecessors()};
                    };
//                dtm.projectManager.getCurrentProject().addActivity();
                }

                for (; i < PMTable.MAX_TABLE_SIZE; i++) {
                    tableRows[i] = new String[]{""};
                }

                dtm.setDataVector(tableRows, columnNames);

                projectManager.getActivityList().createGraph();
            }
        }
    }

    public PMTable getActivitiesTable() {
        return (PMTable) activitiesTable;
    }

    private void createUIComponents() {

        tableRows = new Object[PMTable.MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID, PMTable.NAME, PMTable.DURATION, PMTable.START, PMTable.FINISH, PMTable.PREDECESSORS};
        dtm = new PMTableModel(tableRows, columnNames, projectManager);

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

        panel1 = new JPanel(new BorderLayout(), false);
        panel1.setSize(new Dimension(640, 480));

        panel1.add(splitPane);


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

        //  todo: stub for now. Integrate with main project
//        project = new Project(null, "project", "desc", new Date(), new Date());
    }

//    public void drawGraph(ActivityList activityList) {
//
//        activityList.graph.getModel().beginUpdate();
//        try {
//            activityList.linkNodes();
//        } finally {
//            activityList.graph.getModel().endUpdate();
//        }
//
//        activityList.graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
//        autoLayout(activityList.graph);
//    }

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

//        if (e.getActionCommand().equals("New")) {
//            doNewProject();
//        } else if (e.getActionCommand().equals("Open...")) {
//            doOpenProject();
//        } else if (e.getActionCommand().equals("Save As...")) {
//            doSaveProject();
//        } else if (e.getActionCommand().equals("Close")) {
//            doCloseProject();
//        } else if (e.getActionCommand().equals("Delete")) {
//            ActivityEntry.this.deleteActivity();
//        }
//
        repaint();
    }

    public void setCPMData(Object[] nonCriticals, Object[] criticals) {
        projectManager.getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", nonCriticals);
        projectManager.getActivityList().graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticals);
    }

    public void activitySelected(int id) {
        if (previousID != -1 && previousID < projectManager.getCurrentProject().getActivities().size()) {
            projectManager.losingDetailFocus(previousID);
        }

        if (id < projectManager.getCurrentProject().getActivities().size()) {
            projectManager.gainingDetailFocus(id);
        } else {
            projectManager.getActivityDetailRenderer().setUIDetailsFromActivity(new Activity());
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
        return projectManager.getCurrentProject().getProject_id();
    }

//    private void doNewProject() {
//        clear();
//
//        ActivityList activities = fillActivities();
//
//        activities.fillActivities();
//
//        drawGraph(new ActivityList());
//
//    }
//
//    private void doOpenProject() {
//        try {
////            File file = getOpenFilename();
//            File file = new File("/Users/joao/Documents/Home/Education/Concordia/Courses/COMP 354/Project/edu.concordia.comp354/slides_project.db");
//
//            if (file != null) {
//
//                clear();
//                activityList.readFromFile(file);
//
//                setActivities(activityList, true);
//
//                drawGraph(activityList);
//            }
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void doSaveProject() {
//        try {
//            File file = getSaveFilename();
////            File file = new File("/Users/joao/Documents/Home/Education/Concordia/Courses/COMP 354/Project/edu.concordia.comp354/slides_project.db");
//
//            if (file != null) {
//
//                fillActivities().writeToFile(file);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void doCloseProject() {
//    }
//
//    private File getOpenFilename() {
//        JFileChooser chooser = new JFileChooser();
//
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "Project", "db");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showOpenDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
//        }
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            return chooser.getSelectedFile();
//        }
//        return null;
//    }
//
//    private File getSaveFilename() {
//        JFileChooser chooser = new JFileChooser();
//        FileNameExtensionFilter filter = new FileNameExtensionFilter(
//                "Project", "db");
//        chooser.setFileFilter(filter);
//        int returnVal = chooser.showSaveDialog(this);
//        if (returnVal == JFileChooser.APPROVE_OPTION) {
//            System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getAbsolutePath());
//        }
//        String path = chooser.getSelectedFile().getAbsolutePath();
//        if (path.endsWith(".db")) {
//            return chooser.getSelectedFile();
//        }
//        return null;
//    }

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

    class ChartPanel extends mxGraphComponent {
        public ChartPanel(mxGraph graph) {
            super(graph);
        }

        @Override
        protected void paintBackground(Graphics g) {
            super.paintBackground(g);
            Graphics2D g2 = (Graphics2D) g;

//                    setBackground(Color.WHITE);
//
//            g2.clearRect(0,0,getWidth(),getHeight());

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


            LocalDate date = projectManager.getCurrentProject().getStart_date();

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


//        setBackground(Color.WHITE);
//        g.setColor(Color.WHITE);
//        g2.fill(new Rectangle2D.Double(0, 0, 600, 600));
//        setBackground(Color.BLACK);
//        g.setColor(Color.BLACK);

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

    private void deleteActivity() {
        projectManager.deleteActivity(activitiesTable.getSelectedRow());

        ((PMTableModel) activitiesTable.getModel()).removeRow(activitiesTable.getSelectedRow());
        setActivities(true);
        projectManager.getActivityList().createGraph();
    }
}
