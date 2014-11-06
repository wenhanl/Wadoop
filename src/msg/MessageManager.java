package msg;

import net.Client;
import net.NetObject;
import net.Server;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/**
 * Created by wenhanl on 14-11-4.
 */
public class MessageManager {
    // Type means this manager is created in server side or client side
    public static enum Type { SERVER, CLIENT }
    private Client client;
    private Server server;
    private SocketChannel socket;
    private Type type;

    public MessageManager(Client client) {
        this.client = client;
        this.type = Type.CLIENT;
    }

    public MessageManager(Server server, SocketChannel socket){
        this.server = server;
        this.socket = socket;
        this.type = Type.SERVER;
    }

    /**
     * Send message to remote
     * @param message
     */
    public void sendMessage(Message message){

        byte[] msg = new byte[0];
        try {
            msg = Message.serialize(message);
        } catch (IOException e) {
            System.err.println("Fail to serialize: " + e.getMessage());
        }

        if(type == Type.SERVER){
            server.write(socket, msg);
        } else if (type == Type.CLIENT) {
            client.write(msg);
        }
    }

    /**
     * Receive only one message
     * @return Message received
     */
    public Message receiveOneMessage(){
        NetObject ret = null;
        if(type == Type.SERVER){
            ret = server.listen();
        } else if (type == Type.CLIENT) {
            ret = client.listen();
        }
        // Ignore not DATA network packet
        if(ret.type != NetObject.NetType.DATA){
            System.out.println("Not receiving data");
            return null;
        }

        Message msg = null;
        try {
            msg = (Message) Message.deserialize(ret.data);
        } catch (IOException e) {
            System.err.println("Fail to deserialize: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            System.err.println("Fail find class: " + e.getMessage());
        }

        return msg;
    }

    /**
     * Send heartbeat to report aliveness to server
     * Only works for Type = CLIENT
     */
    public void sendHeartbeat(){
        if(type != Type.CLIENT) {
            return;
        }
        Message message = new Message();
        message.setType(Message.Type.HEARTBEAT);
        message.setAddr(client.getLocalAddress());
        sendMessage(message);
    }

}
