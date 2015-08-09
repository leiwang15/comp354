package edu.concordia.comp354.gui.EVA;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;

import javax.swing.*;

/**
 * Created by joao on 15.07.26.
 */
public class EVAPanel extends JScrollPane {
    private final ActivityEntry activityEntry;

    public EVAPanel(ActivityEntry activityEntry, mxGraph graph) {
        super();
        this.activityEntry = activityEntry;
    }

}
