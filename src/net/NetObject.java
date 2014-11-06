package net;

import java.nio.channels.SocketChannel;

/**
 * Created by wenhanl on 14-10-3.
 */
public class NetObject {
    public static enum NetType {
        CONNECTION,
        DATA,
        EXCEPTION
    }

    public NetType type;
    public byte[] data;
    public SocketChannel sock;

}
