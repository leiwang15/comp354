package comp354.Model.AON;

import com.mxgraph.model.mxCell;
import com.mxgraph.view.mxGraph;
import comp354.Model.Activity;

import javax.swing.tree.TreeNode;
import java.util.TreeSet;

/**
 * Created by joao on 15.06.23.
 */
public class ActivityOnNode {

	Activity activity;
	private final mxCell mxCell;

	float ES;
	float LS;
	float EF;
	float LF = Float.MAX_VALUE;
	float maxDuration;
	float aFloat;
	private TreeNode cell;


	public ActivityOnNode(Activity activity, mxCell v) {
		this.activity = activity;
		mxCell = v;
	}

	public String getLabel() {
		return activity.getActivity_name();
	}

	public String getDescription() {
		return activity.getActivity_desc();
	}

	public void considerES(float ES) {

		if (ES > this.ES) {
			setES(ES);
		}
	}

	public void setES(float ES) {
		this.ES = ES;

		EF = ES + activity.getDuration();

		aFloat = LS - ES;
	}

	public float getES() {
		return ES;
	}

	public float getLS() {
		return LS;
	}

	public void setLF(float LF) {
		this.LF = LF;

		LS = LF - activity.getDuration();

		aFloat = LS - ES;
	}

	public float getEF() {
		return EF;
	}

	public void setLS(float LS) {
		this.LS = LS;
		aFloat = LS - ES;
	}

	public float getLF() {
		return LF;
	}

	public mxCell getCell() {
		return mxCell;
	}

	public void forwardPass(mxGraph graph) {

		Object[] outEdges = graph.getOutgoingEdges(mxCell);
		for (Object cell : outEdges) {

			if (cell instanceof mxCell) {
				Object obj = ((mxCell) cell).getTarget().getValue();

				if (obj instanceof ActivityOnNode) {
					ActivityOnNode activityOnNode = (ActivityOnNode) obj;

					if (EF > activityOnNode.getES()) {

						activityOnNode.setES(EF);
						activityOnNode.forwardPass(graph);
					}
				}
			}
		}
	}

	public ActivityOnNode findLatestActivity(mxGraph graph) {

		ActivityOnNode latestActivity = this;
		Object[] outEdges = graph.getOutgoingEdges(mxCell);
		for (Object cell : outEdges) {

			if (cell instanceof mxCell) {
				Object obj = ((mxCell) cell).getTarget().getValue();

				if (obj instanceof ActivityOnNode) {
					ActivityOnNode activityOnNode = (ActivityOnNode) obj;

					if (activityOnNode.getEF() > EF) {

						ActivityOnNode test = activityOnNode.findLatestActivity(graph);
						if (test.getEF() > latestActivity.getEF()) {
							latestActivity = test;
						}
					}
				}
			}
		}

		return latestActivity;
	}

	public void backwardPass(mxGraph graph) {

		Object[] inEdges = graph.getIncomingEdges(mxCell);
		for (Object cell : inEdges) {
			if (cell instanceof mxCell) {
				Object obj = ((mxCell) cell).getSource().getValue();

				if (obj instanceof ActivityOnNode) {
					ActivityOnNode parent = (ActivityOnNode) obj;

					if (LS < parent.getLF()) {

						parent.setLF(LS);

						parent.backwardPass(graph);
					}
				}
			}
		}
	}

	public void findCriticalPathCells(mxGraph graph, TreeSet<mxCell> critical) {

		Object[] outEdges = graph.getOutgoingEdges(mxCell);
		for (Object cell : outEdges) {

			if (cell instanceof mxCell) {
				Object obj = ((mxCell) cell).getTarget().getValue();

				if (obj instanceof ActivityOnNode) {
					ActivityOnNode activityOnNode = (ActivityOnNode) obj;

					if (activityOnNode.getFloat() == 0) {

						critical.add(activityOnNode.getCell());
					}

					activityOnNode.findCriticalPathCells(graph, critical);
				}
			}
		}
	}

	public float getFloat() {
		return aFloat;
	}

	@Override
	public String toString() {
		return "(" + aFloat + ")";
	}

	public Activity getActivity() {
		return activity;
	}
}
