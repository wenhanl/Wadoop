import whfs.NameNode;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Master {

    public static void main(String[] args){
        // Start NameNode daemon
        NameNode nameNode = new NameNode();
        nameNode.start();

        // Start User Console
        Console console = new Console();
        console.start();
    }
}
