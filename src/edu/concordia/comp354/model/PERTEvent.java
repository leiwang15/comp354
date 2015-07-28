package edu.concordia.comp354.model;

/**
 * Created by joao on 15.07.27.
 */
public class PERTEvent {
    private final int eventNo;
    private float T;
    private float s;
    private float t;

    public PERTEvent(int eventNo) {

        this.eventNo = eventNo;
    }

    public int getEventNo() {
        return eventNo;
    }

    public float getT() {
        return T;
    }

    public void setT(float t) {
        T = t;
    }

    public float get_s() {
        return s;
    }

    public void set_s(float s) {
        this.s = s;
    }

    public float get_t() {
        return t;
    }

    public void set_t(float t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "";
//        final StringBuilder sb = new StringBuilder();
//        sb.append(eventNo);
//        return sb.toString();
    }
}
