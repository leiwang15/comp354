package edu.concordia.comp354.model;

import edu.concordia.comp354.controller.ActivityController;
import edu.concordia.comp354.controller.ProjectController;
import edu.concordia.comp354.controller.UserController;

import java.time.LocalDate;
import java.util.*;

/**
 * Created by joao on 15.07.12.
 */
public class ProjectManager {

    public static final String ROLE_DEVELOPER = "dev";
    protected List<Project> projectList;
    private User currentUser;
    Project currentProject;

    ActivityList activityList;

    IProjectRenderer projectRenderer;
    IActivityDetailRenderer activityDetailRenderer;
    IActivityEntryRenderer activityEntryRenderer;
    Map<String, User> userMap;
    List<User> userList;

    public ProjectManager() {
    }

    public ActivityList getActivityList() {
        return activityList;
    }

    public IActivityDetailRenderer getActivityDetailRenderer() {
        return activityDetailRenderer;
    }

    public void setActivityDetailRenderer(IActivityDetailRenderer activityDetailRenderer) {
        this.activityDetailRenderer = activityDetailRenderer;
    }

    public IActivityEntryRenderer getActivityEntryRenderer() {
        return activityEntryRenderer;
    }

    public void setActivityEntryRenderer(IActivityEntryRenderer activityEntryRenderer) {
        this.activityEntryRenderer = activityEntryRenderer;
    }

    public IProjectRenderer getProjectRenderer() {
        return projectRenderer;
    }

    public void setActivityList(ActivityList activityList) {
        this.activityList = activityList;
    }

    public void setProjectRenderer(IProjectRenderer projectRenderer) {
        this.projectRenderer = projectRenderer;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }

    public void loadProject(Project project) {

        project.setActivities(new ActivityController().getActByProjectId(project.getProject_id()));

        List<Activity> projectActivities = project.getActivities();

        for (int i1 = 0; i1 < projectActivities.size(); i1++) {
            Activity activity = projectActivities.get(i1);
            activity.setActivity_id(i1 + 1);
            //  get predecessors
            List<Integer> dbPreds = new ActivityController().getActPredecessors(activity.getDBID());
            if (!dbPreds.isEmpty()) {
                ArrayList<Integer> uiPreds = new ArrayList<>();
                for (Integer i : dbPreds) {
                    List<Activity> l = new ActivityController().getActByActId(i);
                    for (Activity act : l) {
                        for (int id = 0; id < projectActivities.size(); id++) {
                            if (projectActivities.get(id).getDBID() == act.getDBID()) {
                                uiPreds.add(id + 1);
                            }
                        }
                    }
                }

                activity.setPredecessors(uiPreds);
                activity.setDirty(DirtyAware.DirtyLevels.UNTOUCHED);
            }
        }

        project.setDirty(DirtyAware.DirtyLevels.UNTOUCHED);
    }

    public void saveProject() {

        if (currentProject.isNew()) {
            new ProjectController().addProject(currentProject, currentUser);
            currentProject.written();
        } else if (currentProject.isModified()) {
            new ProjectController().updateProject(currentProject);
            currentProject.written();
        }

        List<Activity> activities = currentProject.getActivities();

        for (Activity activity : activities) {
            if (activity.isNew()) {
                activity.setDBID(new ActivityController().addActivity(activity));

                for (User user : activity.getUsers()) {
                    new ActivityController().assignUserToActivity(user, activity);
                }
                for (int pred : activity.getPredecessors()) {
                    new ActivityController().setActPredecessor(activity.getDBID(), activities.get(pred - 1).getDBID());
                }
                activity.written();
            } else if (activity.isModified()) {
                new ActivityController().updateActivity(activity);

                new ActivityController().updateActivity(activity.getDBID(), activity.getProgress(), activity.getActivity_desc());

                //  delete user and predecessor assignments
                new ActivityController().deleteAssign(activity.getDBID());
                new ActivityController().severPredecessorsFromActivity(activity.getDBID());

//                assert activity.getUsers() != null : "Critical: No user(s) for activity.";
                if (activity.getUsers() != null) {
                    for (User user : activity.getUsers()) {
                        new ActivityController().assignUserToActivity(user, activity);
                    }
                }
                //  add new predecessors
                if (activity.getPredecessors() != null) {
                    for (int pred : activity.getPredecessors()) {
                        new ActivityController().setActPredecessor(activity.getDBID(), activities.get(pred - 1).getDBID());
                    }
                }

                activity.written();
            }
        }

        //  delete flagged activities

        for (int dbID : currentProject.getActivityDeleteList()) {
            new ActivityController().deleteAct(dbID);
            new ActivityController().severPredecessorsFromActivity(dbID);
            new ActivityController().deleteAssign(dbID);
        }
    }

    public List<Project> loadProjects() {
        projectList = new ProjectController().getProjectsByUser(currentUser);

        for (Project p : projectList) {
            loadProject(p);
        }

        projectRenderer.setProjectList();

        return projectList;
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    public void setCurrentProject(String s) {

        for (int i = 0; i < projectList.size(); i++) {
            Project p = projectList.get(i);
            if (p.getProject_name().equals(s)) {
                setCurrentProject(p);
                if (projectRenderer != null) {
                    projectRenderer.setCurrentProject(i);
                }
                if (activityEntryRenderer != null) {
                    activityEntryRenderer.setActivityList();
                }
                break;
            }
        }
    }

    private void setCurrentProject(Project p) {
        currentProject = p;

        activityList = new ActivityList(activityEntryRenderer, this);

//        if ( currentProject != null ) {
//            activityList.setActivities(currentProject.getActivities());
//        }
    }

    public List<Project> getProjectList() {
        return projectList;
    }

    public void createProject(String projectName, String projectDescription, LocalDate startDate, LocalDate endDate) {
        Project project = new Project(currentUser, projectName, projectDescription, startDate, endDate);
        project.setActivities(new ArrayList<>());
        projectList.add(project);
        projectRenderer.setProjectList();
        setCurrentProject(project.getProject_name());
    }

    public void activityChanged() {
        getActivityEntryRenderer().fillActivityList();
        getActivityList().createGraph();
    }

    public void losingDetailFocus(int id) {
        Activity activity = currentProject.getActivities().get(id);

        activityDetailRenderer.getActivityDetailsFromUI(activity);
    }

    public void gainingDetailFocus(int id) {
        Activity activity = currentProject.getActivities().get(id);

        activityDetailRenderer.setUIDetailsFromActivity(activity);
    }

    public void deleteProject() {
        List<Activity> la = new ActivityController().getActByProjectId(getCurrentProject().getProject_id());

        Iterator<Activity> it = la.iterator();
        for (Activity activity : la) {

            new ActivityController().deleteAct(activity.getDBID());

            new ActivityController().severPredecessorsFromActivity(activity.getDBID());

            new ActivityController().deleteAssign(activity.getDBID());
        }

        new ProjectController().deleteProject(getCurrentProject().getProject_id());

        setCurrentProject((Project) null);
        loadProjects();

        activityEntryRenderer.clear();
    }

    public void deleteActivity(int id) {
        if (0 <= id && id < currentProject.getActivities().size()) {
            List<Activity> activities = currentProject.getActivities();
            for (int i = id + 1; i < activities.size(); i++) {
                Activity a = activities.get(i);

                a.setActivity_id(a.getActivity_id() - 1);

                List<Integer> preds = a.getPredecessors();
                if (preds != null) {
                    for (int j = 0; j < preds.size(); j++) {
                        if (preds.get(j) > id) {
                            preds.set(j, preds.get(j) - 1);
                        } else if (preds.get(j) == id) {
                            preds.remove(j);
                        }
                    }
                }

            }
            Activity deletedAct = activities.remove(id);

            currentProject.flagDeletedActivity(deletedAct);
        }
    }

    public List<User> getUserList(List<String> list) {

        List<User> userList = new ArrayList<>();
        for (String name : list) {
            userList.add(getUser(name));
        }

        return userList;
    }

    public User getUser(String userName) {
        return userMap.get(userName);
    }

    public List<String> getUserNames() {

        List<String> list = new ArrayList<>();
        for (User user : userList) {
            list.add(user.getUserName());
        }

        return list;
    }

    public List<User> loadUsers() {
        userList = new UserController().getUserByRole(ROLE_DEVELOPER);

        userMap = new HashMap<>();
        for (User user : userList) {
            userMap.put(user.getUserName(), user);
        }

        return userList;
    }

    public void createTestUsers() {

        new User("John", "Tesh", ProjectManager.ROLE_DEVELOPER, "John", "John");
        new User("Jack", "Black", ProjectManager.ROLE_DEVELOPER, "Jack", "Jack");
        new User("Fred", "Flintstone", ProjectManager.ROLE_DEVELOPER, "Fred", "Fred");
        new User("Jane", "Fonda", ProjectManager.ROLE_DEVELOPER, "Jane", "Jane");
        new User("June", "Carter", ProjectManager.ROLE_DEVELOPER, "June", "June");
    }

    public void initialize() {
        loadUsers();
    }
}
