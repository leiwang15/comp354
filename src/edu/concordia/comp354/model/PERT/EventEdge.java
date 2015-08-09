package edu.concordia.comp354.model.PERT;

/**
 * Created by joao on 15.07.26.
 */
public class EventEdge {
    int orgEvent;
    int destEvent;

    public EventEdge() {
        orgEvent = destEvent = -1;
    }

    public EventEdge(int orgEvent, int destEvent) {
        this.orgEvent = orgEvent;
        this.destEvent = destEvent;
    }

    public int getOrgEvent() {
        return orgEvent;
    }

    public int getDestEvent() {
        return destEvent;
    }
}
