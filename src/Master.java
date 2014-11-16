
import Tracker.JobTracker;
import whfs.NameNode;


import java.io.IOException;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Master {

    public static void main(String[] args) throws IOException {

        System.out.println("###############################################################\n" +
                "#                    Welcome to Wadoop                        # \n" +
                "#              Author: Guanjie Chen, Wenhan Lu                #\n" +
                "#               Enjoy your MapReduce Jobs!!!                  #\n" +
                "###############################################################");

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
