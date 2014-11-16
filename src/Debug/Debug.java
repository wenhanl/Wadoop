package Debug;

/**
 * Created by wenhanl on 14-11-11.
 */
public class Debug {
    private static boolean debug = true;

    public static void print(String msg){
        if(debug){
            System.out.println(msg);
        }
    }
}
