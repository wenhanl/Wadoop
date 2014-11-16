import Tracker.TaskTracker;
import config.Config;
import whfs.DataNode;

import java.io.IOException;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Slave {

    public static void main(String[] args) throws IOException {
        if(args.length != 1){
            System.out.println("Usage: java Slave <Master_address>");
            return;
        }

        DataNode dataNode = new DataNode(args[0]);
        dataNode.start();

        TaskTracker tasktracker = new TaskTracker(Config.TASK_PORT);
        tasktracker.start();
    }
}
