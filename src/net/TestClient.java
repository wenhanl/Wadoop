package net;

import lombok.Synchronized;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * This is Test Client for net package. Echo server client.
 *
 * Created by wenhanl on 14-10-4.
 */
public class TestClient {
    public static Client client;

    public static void main(String[] args){
        try {
            client = new Client("localhost", 15605);
            Thread listen = new Thread(new Runnable() {
                @Override
                public void run() {
                    boolean closed = false;
                    while(!closed){
                        NetObject obj = client.listen();

                        switch (obj.type){
                            case DATA:
                                try {
                                    System.out.println(new String(obj.data,"UTF-8"));
                                    System.out.print("--> ");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                break;
                            case EXCEPTION:
                                closed = true;
                                System.out.println("Connection reset");
                                break;
                            default:
                                System.out.println("Type Error");
                        }
                    }
                }
            });

            listen.start();
        } catch (IOException e) {
            e.printStackTrace();
        }




        BufferedReader buffInput = new BufferedReader(new InputStreamReader(System.in));
        String cmdInput = "";
        while(true) {
            System.out.print("--> ");
            try {
                cmdInput = buffInput.readLine();
                if (cmdInput == null) break;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
            client.write(cmdInput.getBytes(Charset.forName("UTF-8")));
        }


    }


}
