package msg;
import static msg.MPMessage.MessageType.*;
import mapr.Record;
import mapr.ReducerTask;
import mapr.Task;

import java.util.List;
import java.util.Set;

/**
 * Created by CGJ on 14-11-13.
 */
public class MPPartitionMessage extends MPMessage {
    private int reducerNum;
    private List<Record<String, String>> partitionedRecords;
    private ReducerTask reducertask;

    // Ask for and return the partitioned results from a previously run map job
    public MPPartitionMessage(int reducerNum, List<Record<String, String>> records, ReducerTask reducetask) {
        super(PARTITION, reducerNum);
        this.reducerNum = reducerNum;
        this.partitionedRecords = records;
        this.reducertask = reducetask;
    }

    public int getReducerNum() {
        return reducerNum;
    }

    public ReducerTask getReducertask() {return reducertask;}

    public List<Record<String, String>> getPartitionedRecords() {
        return partitionedRecords;
    }
}
