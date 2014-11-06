package whfs;

import config.Config;
import msg.Message;
import net.NetObject;
import net.Server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


/**
 * NameNode of our distributed file system
 * Created by wenhanl on 14-11-3.
 */
public class NameNode extends Thread {
    // Map from fileName to block list
    private HashMap<String, ArrayList<Integer>> fileBlock = null;

    // Map of registered (alive) data nodes
    private HashMap<String, SocketChannel> dataNodes = null;

    // Map of data node socket to last heartbeat time (milisecond);
    private HashMap<String, Integer> nodeLastHeartbeat = null;

    // Map of DataNode to data blocks
    private HashMap<String, ArrayList<Integer>> nodeBlocks = null;

    // Map of block to DataNode
    private HashMap<Integer, String> blockToNode = null;

    public NameNode(){
        dataNodes = new HashMap<>();
        nodeLastHeartbeat = new HashMap<>();
        nodeBlocks = new HashMap<>();
        blockToNode = new HashMap<>();
        fileBlock = new HashMap<>();
    }

    @Override
    public void run() {
        Server server = new Server(Config.NAMENODE_PORT);

        startHeartbeatDaemon();

        while (true) {
            NetObject obj = server.listen();

            try {
                switch (obj.type) {
                    case DATA:
                        Message msg = (Message) Message.deserialize(obj.data);
                        handleMsg(msg);
                        break;
                    case CONNECTION:
                        String addr = obj.sock.getRemoteAddress().toString();
                        System.out.println("Connection estanblished from " + addr);

                        // Register new DataNode
                        addDataNode(addr, obj.sock);

                        break;
                    case EXCEPTION:
                        System.out.println("Some slave disconnected");
                        break;
                    default:
                        System.out.println("Type Error");
                }
            } catch (IOException | ClassNotFoundException e){
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Start a heartbeat daemon as a background daemon
     * Check every 2 seconds, delete data nodes not heartbeat for a configurable time.
     */
    private void startHeartbeatDaemon(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(dataNodes.size() == 0){
                        continue;
                    }
                    Iterator<String> iterator = dataNodes.keySet().iterator();
                    while (iterator.hasNext()) {
                        String addr = iterator.next();
                        int time = nodeLastHeartbeat.get(addr);
                        time += 2000;
                        if (time >= Config.HEARTBEAT_TIMEOUT){
                            System.out.println("DataNode " + addr + " timeout");
                            deleteDataNode(addr);
                        }
                        nodeLastHeartbeat.put(addr, time);
                    }
                }
            }
        }).start();
    }

    /**
     * Add dataNode to both maps to keep consistency
     * @param key
     * @param sock
     */
    private void addDataNode(String key, SocketChannel sock){
        dataNodes.put(key, sock);
        nodeLastHeartbeat.put(key, 0);
    }

    /**
     * Delete dataNode from both maps to keep consistency
     * @param addr
     */
    private void deleteDataNode(String addr){
        nodeLastHeartbeat.remove(addr);
        dataNodes.remove(addr);
    }

    private void handleMsg(Message msg){
        switch(msg.getType()){
            case HEARTBEAT:
                // Reset wait time to zero
                nodeLastHeartbeat.put(msg.getAddr().toString(), 0);
                break;
        }
    }
}
