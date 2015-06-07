package pm.Pert;

/**
 * Created by joao on 15-05-26.
 */
public class PERTActivity {
    String activity_label;
    String precedents;
    float optimistic;
    float most_likely;
    float pessimistic;
    float expected;
    float stdev;

    public String getActivity_label() {
        return activity_label;
    }

    public void setActivity_label(String activity_label) {
        this.activity_label = activity_label;
    }

    public String getPrecedents() {
        return precedents;
    }

    public void setPrecedents(String precedents) {
        this.precedents = precedents;
    }

    public float getOptimistic() {
        return optimistic;
    }

    public void setOptimistic(float optimistic) {
        this.optimistic = optimistic;
    }

    public float getMost_likely() {
        return most_likely;
    }

    public void setMost_likely(float most_likely) {
        this.most_likely = most_likely;
    }

    public float getPessimistic() {
        return pessimistic;
    }

    public void setPessimistic(float pessimistic) {
        this.pessimistic = pessimistic;
    }

    public void computeExpected() {
        this.expected = (optimistic + 4 * most_likely + pessimistic) / 6;
    }

    public void computeStdev() {
        this.stdev = (pessimistic - optimistic) / 6;
    }
}
