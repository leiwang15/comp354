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
import edu.concordia.comp354.model.PERT.EventLeg;
import edu.concordia.comp354.model.PERT.PERTNetwork;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;

import java.time.LocalDate;
import java.util.*;

public class ActivityList {

    private final ProjectManager projectManager;
    public mxGraph graph;
    public Object parent;
    private ActivityOnNode lastActivity;
    private int[] actualCalendarDayOffsets;
    public boolean hasCycles;

    IActivityEntryRenderer renderer;

    public ActivityList(IActivityEntryRenderer renderer, ProjectManager projectManager) {
        this.projectManager = projectManager;
        graph = new mxGraph();
        parent = graph.getDefaultParent();

        assert renderer != null : "ActivityList: renderer is null";
        this.renderer = renderer;
    }

    public void fillActivities() {
        renderer.fillActivityList();
    }

//    public void setActivities(List<Activity> activities) {
//        this.activities = activities;
//    }

    public mxGraph createGanttChart() {

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

        HashMap<Integer, mxCell> activityID2mxCell = new HashMap<Integer, mxCell>();

        List<Activity> activities = projectManager.getCurrentProject().getActivities();
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
                if (childActivity.getPredecessors() != null && childActivity.getPredecessors().contains(parentActivity.getActivity_id())) {
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

    public mxGraph createPERTChart() {

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

        createPERTNetwork();

        graph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        renderer.autoLayout(graph);

        return graph;
    }


    public void createPERTNetwork() {

        List<Activity> activities = projectManager.getCurrentProject().getActivities();

        PERTNetwork network = new PERTNetwork();
        HashMap<Integer, EventLeg> eventTable = network.createNetwork(activities);

        List<mxCell> events = new ArrayList<>();
        for (int i = 0; i <= network.getNextEventID(); i++) {
            events.add((mxCell) graph.insertVertex(parent,
                    null,
                    null,
                    0,  //	x
                    0,  //	y
                    60,  //	width
                    60,  //	height
                    "rounded=0"));
        }


        for (Activity activity : activities) {

            EventLeg leg = eventTable.get(activity.getActivity_id());

            String label = activity.getActivity_name()+"\nt="+activity.getExpectedDate()+"\ns="+activity.getStdev();
            graph.insertEdge(parent, null, label, events.get(leg.getOrgEvent()), events.get(leg.getDestEvent()));
        }
    }

    private void computeActualCalendarDuration(int projectDuration) {
        DateCalculator<LocalDate> dateCalculator = LocalDateKitCalculatorsFactory.forwardCalculator("Canada");

        //  set and move from start date; destructive to dateCalculator. Must reset start date later
        assert getStartDate() != null : "getStartDate() == null";
        dateCalculator.setStartDate(getStartDate());
        dateCalculator.moveByDays(projectDuration);

        actualCalendarDayOffsets = new int[365];

        //  reset start date
        assert getStartDate() != null : "getStartDate() == null";
        dateCalculator.setStartDate(getStartDate());
        for (int i = 0; i < projectDuration; i++) {
            while (dateCalculator.isCurrentDateNonWorking()) {
                dateCalculator.setCurrentBusinessDate(dateCalculator.getCurrentBusinessDate().plusDays(1));
            }
            actualCalendarDayOffsets[i] = (int) (dateCalculator.getCurrentBusinessDate().toEpochDay() - getStartDate().toEpochDay()) + getStartDate().getDayOfWeek().getValue();
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

            int offset = es + ((ActivityOnNode) cell.getValue()).getActivity().getDuration() - 1;
            assert 0 <= offset && offset < actualCalendarDayOffsets.length : "offset = " + offset;

            int actualDuration = actualCalendarDayOffsets[offset] - startDay + 1;

            geometry.setX(startDay * renderer.getXScale() + renderer.getXGap());
            geometry.setWidth(actualDuration * renderer.getXScale() + renderer.getXGap());

            cell.setGeometry(geometry);
        }
    }

    public boolean hasCycles() {
        hasCycles = false;

        projectManager.activityChanged();

        return hasCycles(projectManager.getActivityList().graph) || hasCycles;
    }

    public boolean hasCycles(mxGraph graph) {
        mxAnalysisGraph graphAnalysis = new mxAnalysisGraph();
        graphAnalysis.setGraph(graph);

        return mxGraphStructure.isCyclicDirected(graphAnalysis);
    }

    public LocalDate getStartDate() {
        return projectManager.getCurrentProject().getStart_date();
    }

    public List<Activity> getActivities() {
        return projectManager.getCurrentProject().getActivities();
    }

    public int size() {
        return getActivities().size();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ActivityList{");
        sb.append(", lastActivity=").append(lastActivity);
        sb.append(", hasCycles=").append(hasCycles);
        sb.append('}');
        return sb.toString();
    }
}
