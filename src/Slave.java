import whfs.DataNode;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Slave {

    public static void main(String[] args){
        if(args.length != 1){
            System.out.println("Usage: java Slave <Master_address>");
        }

        DataNode dataNode = new DataNode(args[0]);
        dataNode.start();
    }
}
