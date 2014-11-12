import debug.Debug;
import whfs.NameNode;

import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Master {

    public static void main(String[] args){

        BlockingDeque<String> fsMsgQueue = new LinkedBlockingDeque<>();

        // Start NameNode daemon
        NameNode nameNode = new NameNode(fsMsgQueue);
        nameNode.start();


        // Start User Console
        Console console = new Console(fsMsgQueue);
        console.start();


    }
}
