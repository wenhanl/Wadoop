package whfs;

import config.Config;
import Debug.Debug;
import file.FileManager;
import msg.MessageManager;
import net.Client;
import net.NetObject;
import net.Server;
import java.io.*;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;


/**
 * Created by wenhanl on 14-11-3.
 */
public class DataNode extends Thread{
    private Client heartbeatClient = null;
    private MessageManager heartbeat = null;
    private Server fileServer = null;
    private String remoteAddr = null;

    public DataNode(String remoteAddr){
        this.remoteAddr = remoteAddr;
    }

    @Override
    public void run() {
        try {
            heartbeatClient = new Client(remoteAddr, Config.NAMENODE_PORT);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return;
        }
        heartbeat = new MessageManager(heartbeatClient);

        // Start a thread to send heartbeats
        Thread sendHeartbeat = startHeartbeat();
        sendHeartbeat.start();

        // Start another thread to receive files
        startFileServer();

        boolean closed = false;
        while (!closed) {
            NetObject obj = heartbeatClient.listen();

            switch (obj.type) {
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

    /**
     * Start sending heartbeat daemon
     * @return daemon thread
     */
    private Thread startHeartbeat(){
        return new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        System.err.println(e.getMessage());
                        break;
                    }

                    heartbeat.sendHeartbeat();
                }
            }
        });
    }

    /**
     * Start a file server daemon
     */
    private void startFileServer() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Start a file server
                fileServer = new Server(Config.DATANODE_FILE_PORT, true);

                // Create a directory for temp file storage
                File tempDir = FileManager.createDir(Config.WHFS_TEMP_PATH);

                // Create a directory for whfs data
                File whfsDir = FileManager.createDir(Config.WHFS_BASE_PATH);

                // WHFS base dir
                String whfsBase = Config.WHFS_BASE_PATH;

                while (true) {
                    NetObject obj = fileServer.listen();

                    switch (obj.type) {
                        case DATA:
                            String localStore = tempDir.getAbsolutePath() + "/temp";
                            File temp = new File(localStore);
                            FileManager.receiveFile(localStore, obj.sock);

                            // Retrieve header (blockname)
                            String blockName = FileManager.retrieveHeader(temp);

                            // Move temp to block file specified in header
                            String blockFile = whfsDir.getAbsolutePath() + "/" + blockName;
                            FileManager.mv(temp, blockFile);
                            temp.delete();

                            System.out.println(blockName + " transfer successfully.");

                            // Close socket every time file received
                            try {
                                obj.sock.close();
                            } catch (IOException e) {
                                System.out.println(e.getMessage());
                            }
                            break;
                        case CONNECTION:
                            Debug.print("Connection estanblished");
                            break;
                        case EXCEPTION:
                            System.out.println("Connection reset");
                            break;
                        default:
                            System.out.println("Type Error");
                    }
                }
            }
        }).start();
    }
}
