package comp354.gui;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.layout.hierarchical.mxHierarchicalLayout;
import com.mxgraph.layout.mxParallelEdgeLayout;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxGraph;
import comp354.Controller.DB_Controller;
import comp354.Model.Activity;
import comp354.Model.ActivityList;
import comp354.gui.editors.IntegerEditor;
import comp354.gui.editors.PredecessorEditor;
import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.table.DatePickerCellEditor;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by joao on 15.06.05.
 */
public class ActivityEntry extends JPanel implements ActionListener {
    protected static final int MAX_TABLE_SIZE = 1024;
    protected static final String DATE_FORMAT = "yyyy/MM/dd";
    protected JPanel panel1;
    private JTable activitiesTable;
    private JPanel charts;
    private JScrollPane tablePane;
    private JScrollPane chartPane;
    PMTableModel dtm;
    private ActivityList activityList;
    String[] columnNames;

    String[][] tableRows;
    private DB_Controller project;
    mxGraph graph;
    final Object parent;
    mxGraphComponent graphComponent;
    private boolean hasCycles;


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

        activitiesTable.createDefaultColumnsFromModel();

        activitiesTable.getColumn(PMTable.DURATION).setCellEditor(new IntegerEditor(1, MAX_TABLE_SIZE, this));
        activitiesTable.getColumn(PMTable.START).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.FINISH).setCellEditor(new DatePickerCellEditor(new SimpleDateFormat(DATE_FORMAT)));
        activitiesTable.getColumn(PMTable.PREDECESSORS).setCellEditor(new PredecessorEditor(this));

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
        splitPane.setResizeWeight(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(350);

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
    }

    public mxGraph createGraph() {
        return createGraph(getActivities());
    }

    public mxGraph createGraph(ActivityList activityList) {

        graph.removeCells(graph.getChildCells(parent, true, true));
        graph.removeCells();
//        graph.setCellsMovable(false);

        linkNodes(activityList);

        graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        autoLayout(graph);

        return graph;
    }

    private void linkNodes(ActivityList activityList) {
        ArrayList<Activity> activities = activityList.getActivities();

        HashMap<Integer, mxCell> map = new HashMap<Integer, mxCell>();
        for (int i = 0; i < activities.size(); i++) {
            mxCell v = (mxCell)graph.insertVertex(parent, null, activities.get(i).getActivity_name() + ": " + activities.get(i).getDuration(), i * 40 + 20, i * 40 + 20, activities.get(i).getDuration() * 20, 30);

//            v.setGeometry(new mxGeometry(0,0,10,10));
            map.put(activities.get(i).getActivity_id(), v);
        }

        for (int i = 0; i < activities.size(); i++) {

            Activity parentActivity = activities.get(i);
            for (Activity childActivity : activities) {
                if (childActivity.getPredecessors().contains(parentActivity.getActivity_id())) {
                    mxCell v2 = map.get(childActivity.getActivity_id());

                    if (!hasCycles) {
                        hasCycles = parentActivity.getActivity_id() == childActivity.getActivity_id();
                    }
                    graph.insertEdge(parent, null, "", map.get(parentActivity.getActivity_id()), v2);
                }
            }
        }
    }

    public boolean hasCycles() {
        hasCycles = false;
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

        graphComponent = new mxGraphComponent(graph);

        charts.add(graphComponent, BorderLayout.CENTER);

        new mxHierarchicalLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
        new mxParallelEdgeLayout(graph, SwingConstants.WEST).execute(graph.getDefaultParent());
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
}
