package mapr;

import Tracker.JobTracker;
import config.Config;
import msg.MPMessageManager;
import msg.MPTaskMessage;
import msg.TaskUpdateMessage;
import whfs.NameNode;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by CGJ on 14-11-13.
 */
public class MPCoordinator {

    private Map<Integer, Task> taskMap;
    private Integer reducerCounter;

    public MPCoordinator() {
        this.taskMap = Collections.synchronizedMap(new HashMap<Integer, Task>());
        reducerCounter = 0;
    }

    public void scheduleMapperTasks(List<Task> tasks) {
        for (Task task : tasks) {
            System.out.println("Task received for scheduling:\n  " + task);
            taskMap.put(task.getTaskID(), task);
            if (task instanceof MapperTask) {
                try {
                    MPMessageManager slaveComm = new MPMessageManager(((MapperTask) task).getHostname(), Config.TASK_PORT);
                    slaveComm.sendMessage(new MPTaskMessage(task));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                task.running = true;
            }
        }
    }

    public void scheduleReducerTasks(ReducerTask reducerTask) {
        int partitionNum = reducerCounter % Config.NUM_REDUCERS;
        reducerCounter++;
        reducerTask.setPartitionNum(partitionNum);
        // send tasks to slave to begin processing
        try {
            String hostname = Config.SLAVE_NODES[partitionNum];
            MPMessageManager slaveComm = new MPMessageManager(hostname, Config.TASK_PORT);
            slaveComm.sendMessage(new MPTaskMessage(reducerTask));
            // set running in each task processed to one
            reducerTask.running = true;
            System.out.println("Reducer task" + partitionNum + " launch!");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processTaskUpdateMessage(TaskUpdateMessage msg) {
        final Task targetTask = taskMap.get(msg.getTaskID());

        if (targetTask instanceof MapperTask) {
            // changes status job whose status changed
            targetTask.running = msg.isRunning();
            targetTask.done = msg.isDone();
            // changes waiting status of reduce jobs that have the map job as it's dependant
            for (Task task : taskMap.values()) {
                if (task instanceof ReducerTask) {
                    ReducerTask reducerTask = (ReducerTask) task;
                    reducerTask.setMapperJobStatus(msg.getTaskID(), msg.isDone());
                    if (reducerTask.allMappersAreReady()) {
                        //schedule reducer task on a slave
                        System.out.println("All Mappers detected as ready for reducer task with TaskID " + reducerTask.getTaskID() + ", initiating Reducer");
                        scheduleReducerTasks(reducerTask);
                    }
                }
            }
        } else if (targetTask instanceof ReducerTask) {
            if (msg.isDone()) {
                // Remove dependant maps
                for (int taskID : ((ReducerTask) targetTask).getDependentMapperJobIds()) {
                    taskMap.remove(taskID);
                }
                // Remove reduce job
                taskMap.remove(msg.getTaskID());

                // Store results in user-defined output file
                if (msg.getPayload() instanceof List) {
                    writeOutputReduceRecords((ReducerTask)targetTask, (List<Record>) msg.getPayload());
                }

                JobTracker.verifyAllReducerTaskDone((ReducerTask) targetTask);
            }
        }
    }


    public void writeOutputReduceRecords(ReducerTask task, List<Record> finalReducerResults) {
        // Append to output file
        String MPFinalOutputFile = Config.MAP_RESULTS_FOLDER + "MP_Result_" + task.getInput();
        try {
            FileWriter fw = new FileWriter(MPFinalOutputFile, true);
            for (Record record : finalReducerResults) {
                fw.append(record.getKey() + "\t" + record.getValue() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Wrote " + finalReducerResults.size() + " result records to " + MPFinalOutputFile + "!");
    }
}
