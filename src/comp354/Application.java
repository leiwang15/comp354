package comp354;

import comp354.gui.ActivityEntry;

import javax.swing.*;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    public static void main(String[] args) {
        JFrame frame = new ActivityEntry("ActivityEntry");

        frame.pack();
        frame.setVisible(true);
    }

}
