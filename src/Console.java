import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Console extends Thread{

    @Override
    public void run() {
        BufferedReader buffInput = new BufferedReader(new InputStreamReader(System.in));
        String userInput = null;
        while(true){
            try {
                userInput = buffInput.readLine();
                System.out.println(userInput);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
