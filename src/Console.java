import java.io.*;
import java.util.concurrent.BlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Console extends Thread{

    private BlockingDeque<String> blockingDeque = null;

    public Console(BlockingDeque<String> q){
        blockingDeque = q;
    }

    @Override
    public void run() {
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(System.in));
        String userInput = null;
        while(true){
            try {
                userInput = buffInput.readLine();
                commandHandler(userInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    void commandHandler(String input){
        try {
            blockingDeque.put(input);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
