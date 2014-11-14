package msg;

/**
 * Created by CGJ on 14-11-13.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;


public class MPMessageManager implements Serializable {

    private final String hostname;
    private final int port;
    private Socket sock;


    public MPMessageManager(String hostname, int port) throws IOException {
        this.hostname = hostname;
        this.port = port;

        sock = new Socket(this.hostname, this.port);
    }


    public MPMessageManager(String hostname, String port) throws IOException {
        this.hostname = hostname;
        this.port = Integer.parseInt(port);

        sock = new Socket(this.hostname, this.port);
    }


    public MPMessageManager(Socket sock) {
        this.sock = sock;
        this.hostname = sock.getInetAddress().getCanonicalHostName();
        this.port = sock.getPort();
    }


    public void sendMessage(MPMessage message) throws IOException {
        // Write the message object to socket's output stream, flushing it to the connected host
        ObjectOutputStream sockOut = new ObjectOutputStream(sock.getOutputStream());
        sockOut.writeObject(message);
        sockOut.flush();
    }


    public MPMessage receiveMessage() throws IOException {
        // Listens to the connected inputstream on the socket, returning the RMI message received
        ObjectInputStream sockIn = new ObjectInputStream(sock.getInputStream());
        try {
            return (MPMessage) sockIn.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Error: could not cast returned data as a Message.");
            throw new IOException(e);
        }
    }

    public String getHostname() {
        return hostname;
    }

    public int getPort() {
        return port;
    }

}

