package comp354.pm.GUI;

/**
 * Created by joao on 15-06-02.
 */
public class Application {

    static ActivityEntry frame;

    public static void main(String[] args) {
        frame = new ActivityEntry("ActivityEntry");

        frame.pack();
        frame.setVisible(true);
    }
}
