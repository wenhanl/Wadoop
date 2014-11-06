package msg;

import lombok.Data;

import java.io.*;
import java.net.SocketAddress;

/**
 * Created by wenhanl on 14-11-4.
 */
@Data
public class Message implements Serializable{
    public static enum Type { HEARTBEAT }

    private Type type;
    private SocketAddress addr;


    public static byte[] serialize(Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream os = new ObjectOutputStream(out);
        os.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        ObjectInputStream is = new ObjectInputStream(in);
        return is.readObject();
    }
}
