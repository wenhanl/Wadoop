package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wenhanl on 14-10-4.
 */
public class Client {
    private SocketChannel sc;
    private ByteBuffer readBuffer;
    private int bufLen = 10240;
    private Selector selector;

    /**
     * Init client with hostname and port
     * @param hostname
     * @param port
     */
    public Client(String hostname, int port) throws IOException{
        // Open a socket channel connecting to master node on port 15640
        sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(hostname, port));
        sc.configureBlocking(false);

        selector = Selector.open();
        sc.register(selector, SelectionKey.OP_READ);

        readBuffer = ByteBuffer.allocate(bufLen);
    }

    /**
     * Listen to incoming data
     *
     * @return Exception or Data in a NetObject
     *
     */
    public NetObject listen(){
        NetObject ret = new NetObject();
        byte[] data = null;
        try {
            selector.select();

            Set<SelectionKey> keySet = selector.selectedKeys();

            Iterator<SelectionKey> keyIterator = keySet.iterator();

            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                if (key.isReadable()) {

                    readBuffer.clear();
                    int bufRead = sc.read(readBuffer);
                    readBuffer.flip();

                    if (bufRead == -1) {
                        // Remote connection
                        ret.type = NetObject.NetType.EXCEPTION;
                        continue;
                    }

                    data = new byte[readBuffer.remaining()];
                    // Read bytes from readBuffer to data
                    readBuffer.get(data);
                    ret.type = NetObject.NetType.DATA;
                    ret.data = data;

                    readBuffer.clear();

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public SocketAddress getLocalAddress(){
        SocketAddress ret = null;
        try {
            ret = sc.getLocalAddress();
        } catch (IOException e) {
            System.err.println("Fail to get local address");
        }
        return ret;
    }

    /**
     * Write a byte array to client socket
     * @param bytes
     */
    public void write(byte[] bytes){
        ByteBuffer buffer= ByteBuffer.wrap(bytes);
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
