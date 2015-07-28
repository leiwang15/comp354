package edu.concordia.comp354.model.PERT;

import com.mxgraph.shape.mxArrowShape;
import edu.concordia.comp354.gui.PERT.PERTBoxShape;
import edu.concordia.comp354.model.Activity;

/**
 * Created by joao on 15.07.28.
 */
public class PERTEdge extends mxArrowShape {
    public static final String SHAPE_PERTEDGE = "pert_edge";
    private final Activity activity;

    public PERTEdge() {

        this.activity = null;
    }

    public PERTEdge(Activity activity) {

        this.activity = activity;
    }

    public float get_s() {
        return activity.getStdev();
    }

    public float get_t() {
        return activity.getExpectedDate();
    }

    @Override
    public String toString() {
        return activity.getActivity_name() +
                "\nt=" + PERTBoxShape.df.format(activity.getExpectedDate()) +
                "\ns=" + PERTBoxShape.df.format(activity.getStdev());
    }
}
