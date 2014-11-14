import Debug.Debug;
import Tracker.JobTracker;
import whfs.NameNode;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Master {

    public static void main(String[] args) throws IOException {

        BlockingDeque<String> fsMsgQueue = new LinkedBlockingDeque<>();
        BlockingDeque<String> JTQueue = new LinkedBlockingDeque<>();

        // Start NameNode daemon
        NameNode nameNode = new NameNode(fsMsgQueue);
        nameNode.start();

        // Start Job Tracker
        JobTracker jobtracker = new JobTracker(JTQueue);
        jobtracker.start();

        // Start User Console
        Console console = new Console(fsMsgQueue, JTQueue);
        console.start();


    }
}
