package edu.concordia.comp354.model.PERT;

/**
 * Created by joao on 15.07.26.
 */
public class EventLeg {
    int orgEvent;
    int destEvent;

    public EventLeg() {
        orgEvent = destEvent = -1;
    }

    public EventLeg(int orgEvent, int destEvent) {
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
