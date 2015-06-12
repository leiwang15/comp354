package comp354.Model;

import org.apache.commons.lang3.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ActivityList {

    ArrayList<Activity> activities;

    public ActivityList() {
        this.activities = new ArrayList<Activity>();
    }

    public ActivityList(ArrayList<Activity> activities) {
        this.activities = activities;
    }

    public ArrayList<Activity> getActivities() {
        return activities;
    }

    public void readFromFile(File file) throws FileNotFoundException {

        activities = new ArrayList<>();

        try {
            BufferedReader fis = new BufferedReader(new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8"));

            String line;
            while (StringUtils.isNotEmpty(line = fis.readLine())) {
                StringTokenizer stringTokenizer = new StringTokenizer(line, "\t");

                int activity_id = Integer.parseInt(stringTokenizer.nextToken());
                String name = stringTokenizer.nextToken();
                int duration = Integer.parseInt(stringTokenizer.nextToken());

                ArrayList<Integer> predecessors = new ArrayList<Integer>();
                if (stringTokenizer.hasMoreTokens()) {
                    String predStr = stringTokenizer.nextToken();

                    for (String p : predStr.split(",")) {
                        predecessors.add(Integer.parseInt(p.trim()));
                    }
                }
                activities.add(new Activity(activity_id, 0, name, "", duration, 0, 0, predecessors));
            }

            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    public void writeToFile(File file) throws IOException {
        BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file.getAbsolutePath()), "UTF-8"));

        for (Activity activity : getActivities()) {
            fos.write(Integer.toString(activity.getActivity_id()) + "\t" +
                    activity.getActivity_name() + "\t" +
                    activity.getDuration() + "\t" +
                    Arrays.asList(activity.getPredecessors()).toString().replaceAll("\\[|\\]", "") + "\n");
        }

        fos.close();
    }
}
