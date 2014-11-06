package mapr;

/**
 * Created by wenhanl on 14-11-1.
 */
public abstract class Mapper<InKey, InValue, OutKey, OutValue> {
    public void map(InKey inKey, InValue inValue, OutKey outKey, OutValue outValue){};
    public void print(){};
}
