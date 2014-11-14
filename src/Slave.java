import Tracker.TaskTracker;
import config.Config;
import whfs.DataNode;

import java.io.IOException;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Slave {

    public static void main(String[] args) throws IOException {
        DataNode dataNode = new DataNode();
        dataNode.start();

        TaskTracker tasktracker = new TaskTracker(Config.TASK_PORT);
        tasktracker.start();
    }
}
