package net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by wenhanl on 14-10-3.
 */
public class Server {
    private ServerSocketChannel serverChannel;
    private Selector selector;
    // Num of sockets
    private int connections;
    // Current connections
    private HashMap<Integer, SocketInfo> socks;
    // read buffer
    private ByteBuffer readBuffer;
    private int bufferSize = 10240;
    private boolean fileServer = false;

    public Server(int port){
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            connections = 0;
            socks = new HashMap<>();
            readBuffer = ByteBuffer.allocate(bufferSize);
            fileServer = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server(int port, boolean isFile){
        try {
            serverChannel = ServerSocketChannel.open();
            serverChannel.socket().bind(new InetSocketAddress(port));
            selector = Selector.open();
            serverChannel.configureBlocking(false);

            serverChannel.register(selector, SelectionKey.OP_ACCEPT);
            connections = 0;
            socks = new HashMap<>();
            readBuffer = ByteBuffer.allocate(bufferSize);
            fileServer = isFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public NetObject listen(){
        NetObject ret = new NetObject();
        byte[] data = null;
        try {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                // Incoming connections
                if(key.isAcceptable()){
                    // get server socket
                    ServerSocketChannel ssc = (ServerSocketChannel) key.channel();
                    // accept new socket
                    SocketChannel sc = ssc.accept();
                    // configure new socket as non-blocking
                    sc.configureBlocking(false);
                    // register new socket to selector
                    SelectionKey selectionKey = sc.register(selector, SelectionKey.OP_READ);
                    // save socket to connection map
                    SocketInfo sock = new SocketInfo(sc,selectionKey);
                    socks.put(connections, sock);
                    connections++;

                    ret.type = NetObject.NetType.CONNECTION;
                    ret.sock = sc;

                }
                // Incoming data
                else if(key.isReadable()){
                    SocketChannel sc = (SocketChannel) key.channel();

                    if(fileServer){
                        ret.sock = sc;
                        ret.type = NetObject.NetType.DATA;
                        continue;
                    }

                    readBuffer.clear();
                    // Switch to read mode


                    int bufRead = sc.read(readBuffer);

                    readBuffer.flip();

                    if(bufRead < 0){
                        //System.out.println("connections lost from one slave");
                        // Remove from select list
                        key.cancel();
                        // Remove from sock list
                        Object[] set = socks.keySet().toArray();
                        for(int i = 0; i < set.length; i++){
                            if(socks.get(set[i]).getKey().equals(key)){
                                socks.remove(set[i]);
                            }
                        }
                        ret.type = NetObject.NetType.EXCEPTION;
                        continue;
                    }

                    data = new byte[readBuffer.remaining()];
                    readBuffer.get(data);
                    readBuffer.clear();
                    ret.type = NetObject.NetType.DATA;
                    ret.data = data;
                    ret.sock = sc;

                }
                keyIterator.remove();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return ret;
    }

    public SocketAddress getLocalAddress(){
        SocketAddress ret = null;
        try {
            ret = serverChannel.getLocalAddress();
        } catch (IOException e) {
            System.err.println("Fail to get local address");
        }
        return ret;
    }

    /**
     * Write data to a socket in this server
     * @param sc SocketChannel - socket to write
     * @param bytes byte[] bytes to write
     */
    public void write(SocketChannel sc, byte[] bytes){
        ByteBuffer buffer= ByteBuffer.wrap(bytes);
        try {
            sc.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
