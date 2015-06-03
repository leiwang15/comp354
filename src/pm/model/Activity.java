package pm.model;


public class Activity {
    private int activity_id;
    private String activity_name;
    private String activity_desc;
    private int progress;
    private int duration; //in hours

    /**
     * @return the activity_id
     */
    public int getActivity_id() {
        return activity_id;
    }

    /**
     * @param activity_id the activity_id to set
     */
    public void setActivity_id(int activity_id) {
        this.activity_id = activity_id;
    }

    /**
     * @return the activity_name
     */
    public String getActivity_name() {
        return activity_name;
    }

    /**
     * @param activity_name the activity_name to set
     */
    public void setActivity_name(String activity_name) {
        this.activity_name = activity_name;
    }

    /**
     * @return the activity_desc
     */
    public String getActivity_desc() {
        return activity_desc;
    }

    /**
     * @param activity_desc the activity_desc to set
     */
    public void setActivity_desc(String activity_desc) {
        this.activity_desc = activity_desc;
    }

    /**
     * @return the duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }

	/**
	 * @return the progress
	 */
	public int getProgress() {
		return progress;
	}

	/**
	 * @param progress the progress to set
	 */
	public void setProgress(int progress) {
		this.progress = progress;
	}
}
