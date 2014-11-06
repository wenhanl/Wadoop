package whfs;

import config.Config;
import msg.MessageManager;
import net.Client;
import net.NetObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by wenhanl on 14-11-3.
 */
public class DataNode extends Thread{

    @Override
    public void run() {
        Client client = null;
        try {
            client = new Client("localhost", Config.NAMENODE_PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        final MessageManager msgManager = new MessageManager(client);

        Thread sendHeartbeat = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        break;
                    }

                    msgManager.sendHeartbeat();
                }
            }
        });
        sendHeartbeat.start();

        boolean closed = false;
        while(!closed){
            NetObject obj = client.listen();

            switch (obj.type){
                case DATA:
                    break;
                case EXCEPTION:
                    closed = true;
                    System.out.println("Connection reset");
                    sendHeartbeat.interrupt();
                    break;
                default:
                    System.out.println("Type Error");
            }
        }

    }
}
