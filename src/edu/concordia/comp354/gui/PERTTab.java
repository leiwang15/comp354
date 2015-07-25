package edu.concordia.comp354.gui;

import javax.swing.*;
import java.awt.*;

/**
 * Created by joao on 15.07.25.
 */
public class PERTTab extends ActivityEntry {

    public PERTTab(MainWindow mainWindow) {
        super(mainWindow);
    }

    protected void initializeTab() {

        setName("PERT");
//        setLayout(new BorderLayout());

//        JPanel parentContainer = getParentWindow().getParentContainer(this);
        JPanel parentContainer = new JPanel();
        getParentWindow().tabbedPane.addTab(getName(), null, this, null);

        parentContainer.setLayout(new BorderLayout());

        Dimension dimension = new Dimension(150, 600);
        parentContainer.setMaximumSize(dimension);
        parentContainer.setMinimumSize(dimension);
        parentContainer.setPreferredSize(dimension);
//        this.setLayout(new BorderLayout());
//        Dimension dimension = new Dimension(150, 600);
//        this.setMaximumSize(dimension);
//        this.setMinimumSize(dimension);
//        this.setPreferredSize(dimension);

//        parentContainer.addTab("PERT", null, panel, null);
//        parentContainer.add(panel);
//        parentContainer.add(this);
    }
}
