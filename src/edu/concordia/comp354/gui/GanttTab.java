package edu.concordia.comp354.gui;

import javax.swing.*;
import java.awt.*;

public class GanttTab extends ActivityEntry  {

    public GanttTab(MainWindow mainWindow) {
        super(mainWindow);
    }

    protected void initializeTab() {

        setName("Gantt");
//        setLayout(new BorderLayout());

//        JPanel parentContainer = getParentWindow().getParentContainer(this);

        JPanel parentContainer = new JPanel();
        getParentWindow().tabbedPane.addTab(getName(), null, this, null);
//        tabbedPane.addTab(panel.getName(), null, panel, null);
//        parentPanel.add(panel);

//        parentContainer = this;

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);
//        Dimension dimension = new Dimension(150, 600);
//        this.setLayout(new BorderLayout());
//        this.setMaximumSize(dimension);
//        this.setMinimumSize(dimension);
//        this.setPreferredSize(dimension);

//        parentContainer.addTab("Gantt", null, panel, null);
//        parentContainer.add(this);
    }

    @Override
    public void gainFocus() {
    }
}
