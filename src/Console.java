import java.io.*;
import java.util.concurrent.BlockingDeque;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Console extends Thread{

    private BlockingDeque<String> NameNodeBlockingDeque = null;
    private BlockingDeque<String> JobTrackerBlockingDeque = null;

    public Console(BlockingDeque<String> q, BlockingDeque<String> q2){
        NameNodeBlockingDeque = q;
        JobTrackerBlockingDeque = q2;
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
            if(input.contains(("mprun")))
                JobTrackerBlockingDeque.put(input);
            else
                NameNodeBlockingDeque.put(input);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
