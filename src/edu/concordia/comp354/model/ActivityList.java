package edu.concordia.comp354.model;

import com.mxgraph.analysis.mxAnalysisGraph;
import com.mxgraph.analysis.mxGraphStructure;
import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.model.mxICell;
import com.mxgraph.util.mxConstants;
import com.mxgraph.util.mxRectangle;
import com.mxgraph.view.mxEdgeStyle;
import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.model.AON.ActivityOnNode;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.time.LocalDate;
import java.util.*;

public class ActivityList {

    ArrayList<Activity> activities;
    public mxGraph graph;
    public Object parent;
    private ActivityOnNode lastActivity;
    private int[] actualCalendarDayOffsets;
    public boolean hasCycles;

    IActivityRenderer renderer;
    LocalDate startDate;

    public ActivityList(IActivityRenderer renderer) {
        this.activities = new ArrayList<Activity>();
        graph = new mxGraph();
        parent = graph.getDefaultParent();
        this.renderer = renderer;
    }

    public ArrayList<Activity> getActivities() {
        return activities = renderer.getActivityList();
    }

    public void readFromFile(File file) throws FileNotFoundException {

        activities = new ArrayList<>();

        try {
            BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));

            String line;
            while (StringUtils.isNotEmpty(line = fis.readLine())) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, "\t");

                int activity_id = Integer.parseInt(stringTokenizer.nextToken());
                String name = stringTokenizer.nextToken();
                int duration = Integer.parseInt(stringTokenizer.nextToken());

                ArrayList<Integer> predecessors = new ArrayList<Integer>();
                if (stringTokenizer.hasMoreTokens()) {
                    String predStr = stringTokenizer.nextToken();

                    for (String p : predStr.split(",")) {
                        predecessors.add(Integer.parseInt(p.trim()));
                    }
                }
                activities.add(new Activity(activity_id, 0, name, "", duration, 0, 0, predecessors, 0, 0, 0));
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(File file) throws IOException {
        BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

        for (Activity activity : getActivities()) {
            fos.write(Integer.toString(activity.getActivity_id()) + "\t" +
                    activity.getActivity_name() + "\t" +
                    activity.getDuration() + "\t" +
                    Arrays.asList(activity.getPredecessors()).toString().replaceAll("\\[|\\]", "") + "\n");
        }

        fos.close();
    }

    public mxGraph createGraph() {

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

        linkNodes();

        graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        renderer.autoLayout(graph);

        return graph;
    }

    public void linkNodes() {
        ArrayList<Activity> activities = getActivities();

        HashMap<Integer, mxCell> activityID2mxCell = new HashMap<Integer, mxCell>();

        for (int i = 0; i < activities.size(); i++) {

            mxCell v = (mxCell) graph.insertVertex(parent,
                    null,
                    null,
                    0,                                              //	x
                    i * renderer.getRowHeight() + 14 + 23,   //	y
                    activities.get(i).getDuration() * renderer.getXScale(),      //	width
                    renderer.getRowHeight() - 2,             //	height
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

        TreeSet<mxCell> criticalNodes = calculateCPM();

        float projectDuration = getProjectDuration();

        computeActualCalendarDuration((int) projectDuration);

        positionGANTTNodes(activityID2mxCell);

        renderer.setCPMData(activityID2mxCell.values().toArray(), criticalNodes.toArray());
//
//        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "lightblue", activityID2mxCell.values().toArray());
//        graph.setCellStyles(mxConstants.STYLE_FILLCOLOR, "orange", criticalNodes.toArray());
    }

    private void computeActualCalendarDuration(int projectDuration) {
        DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.forwardCalculator("Canada");

        dateCalculator.setStartDate(this.startDate);
        dateCalculator.moveByDays(projectDuration);

        actualCalendarDayOffsets = new int[projectDuration];

        dateCalculator.setStartDate(startDate);
        for (int i = 0; i < projectDuration; i++) {
            while (dateCalculator.isCurrentDateNonWorking()) {
                dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
            }
            actualCalendarDayOffsets[i] = (int) (dateCalculator.getCurrentBusinessDate().toEpochDay() - startDate.toEpochDay()) + startDate.getDayOfWeek().getValue();
            dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
        }
//        System.out.println(Arrays.toString(actualCalendarDayOffsets));
    }

    private TreeSet<mxCell> calculateCPM() {

        TreeSet<mxCell> criticalNodes = new TreeSet<mxCell>(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                return ((ActivityOnNode) ((mxCell) o1).getValue()).getLabel().compareTo(((ActivityOnNode) ((mxCell) o2).getValue()).getLabel());
            }
        });

        mxCell root = (mxCell) graph.getDefaultParent();

        if (root != null && root.getChildCount() != 0) {
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

    public float getProjectDuration() {
        return lastActivity != null ? lastActivity.getLF() : 0;
    }

    private void positionGANTTNodes(HashMap<Integer, mxCell> activityID2mxCell) {

        for (mxICell cell : activityID2mxCell.values()) {
            mxGeometry geometry = cell.getGeometry();

            final int es = (int) ((ActivityOnNode) cell.getValue()).getES();
            int startDay = actualCalendarDayOffsets[es];
            int actualDuration = actualCalendarDayOffsets[es + ((ActivityOnNode) cell.getValue()).getActivity().getDuration() - 1] - startDay + 1;

            geometry.setX(startDay * renderer.getXScale() + renderer.getXGap());
            geometry.setWidth(actualDuration * renderer.getXScale() + renderer.getXGap());

            cell.setGeometry(geometry);
        }
    }

    public boolean hasCycles() {
        hasCycles = false;

        return hasCycles(createGraph()) || hasCycles;
    }

    public boolean hasCycles(mxGraph graph) {
        mxAnalysisGraph graphAnalysis = new mxAnalysisGraph();
        graphAnalysis.setGraph(graph);

        return mxGraphStructure.isCyclicDirected(graphAnalysis);
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
