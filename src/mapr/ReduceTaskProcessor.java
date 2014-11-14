package mapr;

import config.Config;
import msg.MPMessageManager;
import msg.MPPartitionMessage;
import msg.TaskUpdateMessage;

import java.io.IOException;
import java.util.*;

/**
 * Created by CGJ on 14-11-13.
 */
public class ReduceTaskProcessor extends Thread{
    private ReducerTask task;

    public ReduceTaskProcessor(ReducerTask reduerTask, MPMessageManager commHandler) {
        this.task = reduerTask;
    }

    public void run() {
        List<Record> reducerResults = runReducer(task);
        // We are DONE, TELL MASTER we are done
        try {
            MPMessageManager masterComm = new MPMessageManager(Config.MASTER_NODE, Config.DATA_PORT);
            masterComm.sendMessage(new TaskUpdateMessage(task.getTaskID(), false, true, reducerResults, task.getInput()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<Record> runReducer(ReducerTask reduceTask) {

        // 1. Partition - Ping each Slave and ask Records that belong to task ID and this partition/slaveNum
        List<Record<String, String>> partitionedRecords = new ArrayList<Record<String, String>>();

        for (int i=0;i<Config.SLAVE_NODES.length;i++) {
            try {
                // Ask for the result partitions from each slave for this Job ID
                String hostname = Config.SLAVE_NODES[i];
                MPMessageManager requestHandle = new MPMessageManager(hostname, Config.TASK_PORT);
                requestHandle.sendMessage(new MPPartitionMessage(reduceTask.getPartitionNum(), null, reduceTask));

                MPPartitionMessage partitionMessage = (MPPartitionMessage) requestHandle.receiveMessage();

                if(partitionMessage.getPartitionedRecords()!=null)
                    partitionedRecords.addAll(partitionMessage.getPartitionedRecords());

                System.out.println(partitionedRecords.size() + " records in reducer partition " + reduceTask.getPartitionNum());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 2. Sort - Sort partitionedRecords by key and merge like keys together
        Collections.sort(partitionedRecords, new Comparator<Record<String, String>>() {
            @Override
            public int compare(Record<String, String> record, Record<String, String> record2) {
                return record.getKey().compareTo(record2.getKey());
            }
        });

        if (partitionedRecords.size() == 0) {
            return null;
        }

        // Merge the sorted records by key
        List<Record<String, List<String>>> reducerRecords = new ArrayList<Record<String, List<String>>>();

        List<String> currentValues = new ArrayList<String>();
        String previousKey = partitionedRecords.get(0).getKey();
        currentValues.add(partitionedRecords.get(0).getValue());
        for (int i = 1; i < partitionedRecords.size(); i++) {
            Record<String, String> currentRecord = partitionedRecords.get(i);
            if (!currentRecord.getKey().equals(previousKey)) {
                // Moved on to the next key, add previous Records to reducerRecords
                reducerRecords.add(new Record<String, List<String>>(previousKey, currentValues));
                previousKey = currentRecord.getKey();
                currentValues = new ArrayList<String>();
            }
            currentValues.add(partitionedRecords.get(i).getValue());
        }
        reducerRecords.add(new Record<String, List<String>>(previousKey, currentValues));

        // 3. Reduce - Perform reduce operation on every record (key -> list of all values for that key)
        Reducer reducer = reduceTask.getReducer();
        for (Record<String, List<String>> reducerRecord : reducerRecords) {
            reducer.reduce(reducerRecord.getKey(), reducerRecord.getValue());
        }

        // Done! Return and alert Master that you've finished your job
        return reducer.getReduceOutput();
    }







}
