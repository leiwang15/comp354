package edu.concordia.comp354.gui.EVA;

import com.mxgraph.view.mxGraph;
import edu.concordia.comp354.gui.ActivityEntry;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joao on 15.07.26.
 */
public class EVAPanel extends JScrollPane {
    private final ActivityEntry activityEntry;
    private ChartPanel graphPanel;

    public EVAPanel(ActivityEntry activityEntry, mxGraph graph) {
        super();
        this.activityEntry = activityEntry;


    }

    public void setChart(JFreeChart chart) {
        if (graphPanel == null) {
            graphPanel = new ChartPanel(chart);
            graphPanel.setPreferredSize(new Dimension(500, 270));
        } else {
            graphPanel.setChart(chart);
        }
    }

}
