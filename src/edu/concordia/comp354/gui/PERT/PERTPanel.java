package edu.concordia.comp354.gui.PERT;

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.util.mxConstants;
import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;

import java.util.Map;

/**
 * Created by joao on 15.07.26.
 */
public class PERTPanel extends mxGraphComponent {
    private final ActivityEntry activityEntry;

    public PERTPanel(ActivityEntry activityEntry, mxGraph graph) {
        super(graph);
        this.activityEntry = activityEntry;

        Map<String, Object> style = graph.getStylesheet().getDefaultVertexStyle();
        style.put(mxConstants.STYLE_SHAPE, PERTBoxShape.SHAPE_PERTBOX);
    }
}
