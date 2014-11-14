package mapr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGJ on 14-11-12.
 */
public abstract class Reducer<InKey, InValue, OutKey, OutValue> implements Serializable {
    protected List<Record<OutKey, OutValue>> output;

    public Reducer() {
        output = new ArrayList<Record<OutKey, OutValue>>();
    }

    abstract public void reduce(InKey inkey, List<InValue> inValue);

    public List<Record<OutKey, OutValue>> getReduceOutput() {
        return output;
    }

    public void print() {}
}
