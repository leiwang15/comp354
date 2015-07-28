package edu.concordia.comp354.model.PERT;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;

/**
 * Created by joao on 15.07.27.
 */
public class PERTEvent {
    private final mxCell mxCell;

    private final int eventNo;
    private float T;
    private float s;
    private float t;

    public PERTEvent(mxCell mxCell, int eventNo) {
        this.mxCell = mxCell;

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

    public void forwardPass(mxGraph graph) {

        Object[] outEdges = graph.getOutgoingEdges(mxCell);
        for (Object cell : outEdges) {

            if (cell instanceof mxCell) {
                PERTEdge activity = (PERTEdge) (((mxCell) cell).getValue());
                PERTEvent event = (PERTEvent) (((mxCell) cell).getTarget().getValue());

                float t = get_t() + activity.get_t();

                boolean goForward = false;
                if (t > event.get_t()) {
                    event.set_t(t);
                    goForward = true;
                }

                float s = (float) Math.sqrt(activity.get_s() * activity.get_s() + get_s() * get_s());

                if (s > event.get_s()) {
                    event.set_s(s);
                    goForward = true;
                }

                if (goForward) {
                    event.forwardPass(graph);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "";
//        final StringBuilder sb = new StringBuilder();
//        sb.append(eventNo);
//        return sb.toString();
    }
}
