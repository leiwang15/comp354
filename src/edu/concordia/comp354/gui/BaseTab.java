package edu.concordia.comp354.gui;

import edu.concordia.comp354.model.Project;
import edu.concordia.comp354.model.ProjectManager;

import javax.swing.*;

/**
 * Created by joao on 15.07.25.
 */
public class BaseTab extends JPanel {
    private MainRenderer parentWindow;

    public BaseTab(MainRenderer parentWindow) {
        super();
        this.parentWindow = parentWindow;
    }

    public void setParentWindow(MainRenderer parentWindow) {
        this.parentWindow = parentWindow;
    }

    public MainRenderer getParentWindow() {
        return parentWindow;
    }

    public ProjectManager getProjectManager() {
        return getParentWindow().projectManager;
    }

    public Project getCurrentProject() {
        return getProjectManager().getCurrentProject();
    }

    protected void initializeTab() {
    }

    public void gainFocus() {

    }
}
