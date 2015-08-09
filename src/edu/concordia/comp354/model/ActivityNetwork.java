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
import edu.concordia.comp354.gui.PERT.PERTBoxShape;
import edu.concordia.comp354.gui.PERT.PERTEdge;
import edu.concordia.comp354.model.AON.ActivityOnNode;
import edu.concordia.comp354.model.PERT.EventEdge;
import edu.concordia.comp354.model.PERT.PERTEvent;
import edu.concordia.comp354.model.PERT.PERTNetwork;
import net.objectlab.kit.datecalc.common.DateCalculator;
import net.objectlab.kit.datecalc.jdk8.LocalDateKitCalculatorsFactory;

import java.time.LocalDate;
import java.util.*;

public class ActivityNetwork {

    private final ProjectManager projectManager;
    public mxGraph ganttGraph;
    public mxGraph pertGraph;
    private ActivityOnNode lastActivity;
    private int[] actualCalendarDayOffsets;
    public boolean hasCycles;

    IActivityEntryRenderer renderer;
    HashMap<Integer, mxCell> activityID2mxCell;

    public ActivityNetwork(IActivityEntryRenderer renderer, ProjectManager projectManager) {
        this.projectManager = projectManager;
        ganttGraph = new mxGraph();
        pertGraph = new mxGraph();

        assert renderer != null : "ActivityNetwork: renderer is null";
        this.renderer = renderer;
    }

    public void fillActivities() {
        renderer.fillActivityList();
    }

//    public void fillTable(List<Activity> activities) {
//        this.activities = activities;
//    }

    public mxGraph createAONNetwork() {

        Map<String, Object> style = ganttGraph.getStylesheet().getDefaultEdgeStyle();
        style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SideToSide);
        style.put(mxConstants.STYLE_FILLCOLOR, "default");
        style.put(mxConstants.STYLE_STROKECOLOR, "default");

        ganttGraph.removeCells(ganttGraph.getChildCells(ganttGraph.getDefaultParent(), true, true));
        ganttGraph.removeCells();

        ganttGraph.setCellsSelectable(false);
        ganttGraph.setCellsMovable(false);
        ganttGraph.setCellsEditable(false);
        ganttGraph.setCellsLocked(true);

        linkAONNodes();

        ganttGraph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        renderer.autoLayout(ganttGraph);

        return ganttGraph;
    }

    public Collection<mxCell> getActivityNodes() {
        return activityID2mxCell.values();
    }

    public void linkAONNodes() {

        activityID2mxCell = new HashMap<Integer, mxCell>();

        List<Activity> activities = projectManager.getCurrentProject().getActivities();
        for (int i = 0; i < activities.size(); i++) {

            mxCell v = (mxCell) ganttGraph.insertVertex(ganttGraph.getDefaultParent(),
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
                        ganttGraph.insertEdge(parentCell, null, "", parentCell, v2);
                    }
                }
            }
        }

        TreeSet<mxCell> criticalNodes = calculateCPM();

        float projectDuration = getProjectDuration();

        computeActualCalendarDuration((int) projectDuration);

        positionGANTTNodes();

        renderer.setCPMData(activityID2mxCell.values().toArray(), criticalNodes.toArray());
    }

    public mxGraph createPERTChart() {

        Map<String, Object> style = pertGraph.getStylesheet().getDefaultEdgeStyle();
        style.put(mxConstants.STYLE_EDGE, mxEdgeStyle.SideToSide);
        style.put(mxConstants.STYLE_FILLCOLOR, "default");
        style.put(mxConstants.STYLE_STROKECOLOR, "default");

        pertGraph.removeCells(pertGraph.getChildCells(pertGraph.getDefaultParent(), true, true));
        pertGraph.removeCells();

        pertGraph.setCellsSelectable(false);
        pertGraph.setCellsMovable(false);
//        graph.setCellsEditable(false);
//        graph.setCellsLocked(true);

        createPERTNetwork();

        pertGraph.setMaximumGraphBounds(new mxRectangle(0, 0, 800, 800));
        renderer.autoLayout(pertGraph);

        return pertGraph;
    }


    public void createPERTNetwork() {

        List<Activity> activities = projectManager.getCurrentProject().getActivities();

        PERTNetwork network = new PERTNetwork();
        HashMap<Integer, EventEdge> eventTable = network.createNetwork(activities);

        List<mxCell> events = new ArrayList<>();
        for (int i = 0; i <= network.getNextEventID(); i++) {
            mxCell v = (mxCell) pertGraph.insertVertex(
                    pertGraph.getDefaultParent(),
                    null,
                    null,
                    0,  //	x
                    0,  //	y
                    PERTBoxShape.PERT_BOX_LENGTH,  //	width
                    PERTBoxShape.PERT_BOX_LENGTH,  //	height
                    "rounded=1;editable=1;fillColor=white;strokeColor=black;shape=" + PERTBoxShape.SHAPE_PERTBOX);

            v.setValue(new PERTEvent(v, i + 1));
            events.add(v);
        }


        for (Activity activity : activities) {

            EventEdge leg = eventTable.get(activity.getActivity_id());

            pertGraph.insertEdge(pertGraph.getDefaultParent(),
                    null,
                    new PERTEdge(activity),
                    events.get(leg.getOrgEvent()),
                    events.get(leg.getDestEvent()),
                    "connector;edgeStyle=" + PERTEdge.SHAPE_PERTEDGE);
        }

        ((PERTEvent) (events.get(0).getValue())).forwardPass(pertGraph);
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

        mxCell root = (mxCell) ganttGraph.getDefaultParent();

        if (root != null && root.getChildCount() != 0) {
//			System.out.println("root.getChildCount()=" + root.getChildCount());
            ActivityOnNode activityOnNode = (ActivityOnNode) root.getChildAt(0).getValue();

            activityOnNode.setES(0);

            activityOnNode.forwardPass(ganttGraph);

            lastActivity = activityOnNode.findLatestActivity(ganttGraph);

            lastActivity.setLF(lastActivity.getEF());
            lastActivity.backwardPass(ganttGraph);

            criticalNodes.add(activityOnNode.getMxCell());    //  critical path starts at root
            activityOnNode.findCriticalPathCells(ganttGraph, criticalNodes);
        }

        return criticalNodes;
    }

    public float getProjectDuration() {

//        if ( lastActivity == null ) {
//            createAONNetwork();
//        }

        return lastActivity != null ? lastActivity.getLF() : 0;
    }

    private void positionGANTTNodes() {

        for (mxICell cell : getActivityNodes()) {
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

        return hasCycles(projectManager.getActivityNetwork().ganttGraph) || hasCycles;
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
        final StringBuilder sb = new StringBuilder("ActivityNetwork{");
        sb.append(", lastActivity=").append(lastActivity);
        sb.append(", hasCycles=").append(hasCycles);
        sb.append('}');
        return sb.toString();
    }

    public LocalDate getCalendarDate(int date) {
        return getStartDate().plusDays(actualCalendarDayOffsets[date]);
    }
}
