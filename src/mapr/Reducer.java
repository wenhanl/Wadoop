package mapr;

import java.io.Serializable;

/**
 * Created by wenhanl on 14-11-1.
 */
public class Reducer<InKey, InValue, OutKey, OutValue> implements Serializable {
    public void reduce(InKey inkey, InValue inValue, OutKey outKey, OutValue outValue){};
    public void print(){};
}
