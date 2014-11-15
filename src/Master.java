import whfs.NameNode;


import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Master {

    public static void main(String[] args){

        System.out.println("###############################################################\n" +
                "#                    Welcome to Wadoop                        # \n" +
                "#              Author: Guanjie Chen, Wenhan Lu                #\n" +
                "#               Enjoy your MapReduce Jobs!!!                  #\n" +
                "###############################################################");

        BlockingDeque<String> fsMsgQueue = new LinkedBlockingDeque<>();

        // Start NameNode daemon
        NameNode nameNode = new NameNode(fsMsgQueue);
        nameNode.start();


        // Start User Console
        Console console = new Console(fsMsgQueue);
        console.start();


    }
}
