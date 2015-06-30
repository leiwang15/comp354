package comp354.gui;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import comp354.Model.AON.ActivityOnNode;
import comp354.Model.Activity;
import comp354.Model.ActivityList;
import comp354.Model.Project;
import comp354.gui.editors.IntegerEditor;
import comp354.gui.editors.PredecessorEditor;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.table.DatePickerCellEditor;
import sun.swing.table.DefaultTableCellHeaderRenderer;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;


/**
 * Created by joao on 15.06.05.
 */
public class ActivityEntry extends JPanel implements ActionListener, ItemListener {
    protected static final int MAX_TABLE_SIZE = 1024;
    protected static final String DATE_FORMAT = "yyyy/MM/dd";

    protected static final int X_SCALE = 20;
    protected static final int X_GAP = 0;

    protected JPanel panel1;
    private JTable activitiesTable;
    private JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    PMTableModel dtm;
    private ActivityList activityList;
    String[] columnNames;

    String[][] tableRows;
    mxGraph graph;
    final Object parent;
    mxGraphComponent graphComponent;
    private boolean hasCycles;
    ActivityOnNode lastActivity;
    Project project;
    private Date startDate;
    private int[] actualCalendarDayOffsets;

    public ActivityEntry(JFrame frame) {

        createUIComponents();

        activityList = new ActivityList();
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        graphComponent = new mxGraphComponent(graph);
    }

    /*
        get activities from grid input
    */
    public ActivityList getActivities() {
        return this.activityList = dtm.fillActivityList();
    }

    /*
        Set activities in grid
    */
    private void setActivities(ActivityList activityList, boolean update) {
        clear();

        this.activityList = activityList;

        if (update) {

            ArrayList<Activity> activities = activityList.getActivities();
            for (int i = 0; i < activityList.getActivities().size(); i++) {
                Activity activity = activities.get(i);
                tableRows[i] = new String[]{Integer.toString(activity.getActivity_id()), activity.getActivity_name(), Integer.toString(activity.getDuration()), "", "", activity.getPredecessors().toString().replaceAll("\\[|\\]", "")};
            }
            dtm.setDataVector(tableRows, columnNames);
        }
    }

    public PMTable getActivitiesTable() {
        return (PMTable) activitiesTable;
    }

    private void createUIComponents() {

        tableRows = new String[MAX_TABLE_SIZE][];
        columnNames = new String[]{PMTable.ID, PMTable.NAME, PMTable.DURATION, PMTable.START, PMTable.FINISH, PMTable.PREDECESSORS};
        dtm = new PMTableModel(tableRows, columnNames);

        clear();

        DefaultTableColumnModel scm = new DefaultTableColumnModel();

        activitiesTable = new PMTable(dtm, scm);
        activitiesTable.setCellSelectionEnabled(true);
        activitiesTable.addMouseListener(new PopClickListener());

        activitiesTable.createDefaultColumnsFromModel();

        activitiesTable.getColumn(PMTable.DURATION).setCellEditor(new IntegerEditor(1, MAX_TABLE_SIZE, this));
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
        startDate = new Date();
    }

    public mxGraph createGraph() {
        return createGraph(getActivities());
    }

    public mxGraph createGraph(ActivityList activityList) {

        Map<String, Object> style = graph.getStylesheet().getDefaultEdgeStyle();
        style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SideToSide);
        style.put(mxConstants.STYLE_FILLCOLOR, "default");
        style.put(mxConstants.STYLE_STROKECOLOR, "default");

        graph.removeCells(graph.getChildCells(parent, true, true));
        graph.removeCells();

        graph.setCellsSelectable(false);
        graph.setCellsMovable(false);
        graph.setCellsEditable(false);
        graph.setCellsLocked(true);

        linkNodes(activityList);

        graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        autoLayout(graph);

        return graph;
    }

    private void linkNodes(ActivityList activityList) {
        ArrayList<Activity> activities = activityList.getActivities();

        HashMap<Integer, mxCell> activityID2mxCell = new HashMap<Integer, mxCell>();

        for (int i = 0; i < activities.size(); i++) {

            mxCell v = (mxCell) graph.insertVertex(parent,
                    null,
                    null,
                    0,                                              //	x
                    i * (activitiesTable.getRowHeight() + 2) + 14 + 22,  //	y
                    activities.get(i).getDuration() * X_SCALE,           //	width
                    activitiesTable.getRowHeight() - 2,             //	height
                    "rounded=0");
            v.setValue(new ActivityOnNode(activities.get(i), v));

            activityID2mxCell.put(activities.get(i).getActivity_id(), v);
        }

        for (int i = 0; i < activities.size(); i++) {

            Activity parentActivity = activities.get(i);
            for (Activity childActivity : activities) {
                if (childActivity.getPredecessors().contains(parentActivity.getActivity_id())) {
                    mxCell v2 = activityID2mxCell.get(childActivity.getActivity_id());

                    if (!hasCycles) {
                        hasCycles = parentActivity.getActivity_id() == childActivity.getActivity_id();
                    }
                    mxCell parentCell = activityID2mxCell.get(parentActivity.getActivity_id());
                    if (parentCell != v2) {
                        graph.insertEdge(parentCell, null, "", parentCell, v2);
                    }
                }
            }
        }

        TreeSet<mxCell> criticalNodes = calculateCPM(graph);

        float duration = getProjectDuration();

        computeActualCalendarDuration((int) duration);

        positionGANTTNodes(activityID2mxCell);

        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", activityID2mxCell.values().toArray());
        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticalNodes.toArray());
    }

    private void computeActualCalendarDuration(int duration) {
        DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.forwardCalculator(Locale.getDefault().getCountry());
        LocalDate startDate = this.startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        dateCalculator.setStartDate(startDate);
        dateCalculator.moveByDays(duration);

        actualCalendarDayOffsets = new int[duration];

        dateCalculator.setStartDate(startDate);
        for (int i = 0; i < duration; i++) {
            while (dateCalculator.isCurrentDateNonWorking()) {
                dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
            }
            actualCalendarDayOffsets[i] = (int) (dateCalculator.getCurrentBusinessDate().toEpochDay() - startDate.toEpochDay()) + 1;

            dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
        }
    }

    private void positionGANTTNodes(HashMap<Integer, mxCell> activityID2mxCell) {

        for (mxICell cell : activityID2mxCell.values()) {
            mxGeometry geometry = cell.getGeometry();

            final int es = (int) ((ActivityOnNode) cell.getValue()).getES();
            int startDay = actualCalendarDayOffsets[es];
            int actualDuration = actualCalendarDayOffsets[es + ((ActivityOnNode) cell.getValue()).getActivity().getDuration() - 1] - startDay + 1;

            geometry.setX(startDay * X_SCALE + X_GAP);
            geometry.setWidth(actualDuration * X_SCALE + X_GAP);

            cell.setGeometry(geometry);
        }
    }

    private TreeSet<mxCell> calculateCPM(mxGraph graph) {

        TreeSet<mxCell> criticalNodes = new TreeSet<>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((ActivityOnNode) ((mxCell) o1).getValue()).getLabel().compareTo(((ActivityOnNode) ((mxCell) o2).getValue()).getLabel());
            }
        });

        mxCell root = (mxCell) graph.getDefaultParent();

        if (root != null) {
//			System.out.println("root.getChildCount()=" + root.getChildCount());
            ActivityOnNode activityOnNode = (ActivityOnNode) root.getChildAt(0).getValue();

            activityOnNode.setES(0);

            activityOnNode.forwardPass(graph);

            lastActivity = activityOnNode.findLatestActivity(graph);

            lastActivity.setLF(lastActivity.getEF());
            lastActivity.backwardPass(graph);

            criticalNodes.add(activityOnNode.getCell());    //  critical path starts at root
            activityOnNode.findCriticalPathCells(graph, criticalNodes);
        }

        return criticalNodes;
    }

    public boolean hasCycles() {
        hasCycles = false;
        createGraph(getActivities());
        return hasCycles(createGraph(getActivities())) || hasCycles;
    }

    private boolean hasCycles(mxGraph graph) {
        mxAnalysisGraph graphAnalysis = new mxAnalysisGraph();
        graphAnalysis.setGraph(graph);

        return mxGraphStructure.isCyclicDirected(graphAnalysis);
    }

    public void drawGraph(ActivityList activityList) {

        graph.getModel().beginUpdate();
        try {
            linkNodes(activityList);
        } finally {
            graph.getModel().endUpdate();
        }


        graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        autoLayout(graph);
    }

    private void autoLayout(mxGraph graph) {
        charts.remove(graphComponent);

        graphComponent = new ChartPanel(graph);
//        graphComponent = new mxGraphComponent(graph);

        charts.add(graphComponent, BorderLayout.CENTER);

//        new mxHierarchicalLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
//        new mxParallelEdgeLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
        charts.revalidate();
    }

    private void clear() {
        tableRows[0] = new String[]{"1"};
        for (int i = 1; i < MAX_TABLE_SIZE; i++) {
            tableRows[i] = new String[]{""};
        }

        dtm.setDataVector(tableRows, columnNames);
    }

    /*
        Code actions for menus here
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("New")) {
            doNewProject();
        } else if (e.getActionCommand().equals("Open...")) {
            doOpenProject();
        } else if (e.getActionCommand().equals("Save As...")) {
            doSaveProject();
        } else if (e.getActionCommand().equals("Close")) {
            doCloseProject();
        } else if (e.getActionCommand().equals("Delete")) {
            ActivityEntry.this.deleteActivity();
        }


        repaint();
    }

    private void doNewProject() {
        clear();

        ActivityList activities = getActivities();

        activities.getActivities();

        drawGraph(new ActivityList());

    }

    private void doOpenProject() {
        try {
//            File file = getOpenFilename();
            File file = new File("/Users/joao/Documents/Home/Education/Concordia/Courses/COMP 354/Project/comp354/slides_project.db");

            if (file != null) {

                clear();
                activityList.readFromFile(file);

                setActivities(activityList, true);

                drawGraph(activityList);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void doSaveProject() {
        try {
            File file = getSaveFilename();
//            File file = new File("/Users/joao/Documents/Home/Education/Concordia/Courses/COMP 354/Project/comp354/slides_project.db");

            if (file != null) {

                getActivities().writeToFile(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void doCloseProject() {
        //todo: implement real functionality

        doNewProject();
    }

    private File getOpenFilename() {
        JFileChooser chooser = new JFileChooser();

        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Project", "db");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " + chooser.getSelectedFile().getAbsolutePath());
        }
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    private File getSaveFilename() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Project", "db");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showSaveDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to save to this file: " + chooser.getSelectedFile().getAbsolutePath());
        }
        String path = chooser.getSelectedFile().getAbsolutePath();
        if (path.endsWith(".db")) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    public float getProjectDuration() {
        return lastActivity.getLF();
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

            DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.forwardCalculator(Locale.getDefault().getCountry());
            LocalDate date = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

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

    class PopUpDemo extends JPopupMenu {
        JMenuItem anItem;

        public PopUpDemo() {
            anItem = new JMenuItem("Delete");
            add(anItem);
            anItem.addActionListener(ActivityEntry.this);
        }
    }

    class PopClickListener extends MouseAdapter {
        public void mousePressed(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger())
                doPop(e);
        }

        private void doPop(MouseEvent e) {
            PopUpDemo menu = new PopUpDemo();

            menu.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private void deleteActivity() {
        ((PMTableModel) activitiesTable.getModel()).removeRow(activitiesTable.getSelectedRow());
    }
}
