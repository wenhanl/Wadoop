package net;

import java.io.UnsupportedEncodingException;

/**
 * Test Server for net package. An Echo server
 *
 * Created by wenhanl on 14-10-4.
 */
public class TestServer {

    public static void main(String[] args) {
        Server server = new Server(15605);

        while (true) {
            NetObject obj = server.listen();

            switch (obj.type) {
                case DATA:
                    try {
                        String str = new String(obj.data, "UTF-8");
                        System.out.println(str);
                        // Echo content received
                        server.write(obj.sock, obj.data);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    break;
                case CONNECTION:
                    System.out.println("Connection estanblished");
                    break;
                case EXCEPTION:
                    System.out.println("Connection reset");
                    break;
                default:
                    System.out.println("Type Error");
            }
        }
    }
}

