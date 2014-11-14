package mapr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by CGJ on 14-11-12.
 */
public abstract class Mapper<InKey, InValue, OutKey, OutValue> implements Serializable {
    protected List<Record<OutKey, OutValue>> output;

    public Mapper() {
        output = new ArrayList<Record<OutKey, OutValue>>();
    }

    abstract public void map(InKey inKey, InValue inValue);

    public List<Record<OutKey, OutValue>> getMapOutput() {
        return output;
    }

    public void print() {}
}
