
import file.FileManager;
import mapr.Mapper;
import mapr.Reducer;

import java.io.File;

/**
 * Created by wenhanl on 14-11-1.
 */
public class Test {
    public static void main(String[] args){
        try {
            Class mapper = Class.forName("example.WordCount");
            Class[] classes = mapper.getClasses();
            Mapper map;
            Reducer reducer;
            for(Class clazz : classes){
                if(clazz.getName().equals("example.WordCount$Map")){
                    map = (Mapper) clazz.newInstance();
                    map.print();
                }
                if(clazz.getName().equals("example.WordCount$Reduce")){
                    reducer = (Reducer) clazz.newInstance();
                    reducer.print();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        File old = new File("/tmp/640local/test");
        String tgt = "/tmp/dest/haha";
        FileManager.mv(old, tgt);
    }
}
