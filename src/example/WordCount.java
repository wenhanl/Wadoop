package example;

import mapr.Mapper;
import mapr.Record;
import mapr.Reducer;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by wenhanl on 14-11-1.
 */
public class WordCount {

    public static class Map extends Mapper<Long, String, String, Integer> {
        public static Record<String, Integer> out;

        public void map(Long key, String value, Record<String, Integer> output) throws IOException {
            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);
            while (tokenizer.hasMoreTokens()) {
                String word = tokenizer.nextToken();
                out.set(word, 1);
            }
        }

        public void print(){
            System.out.println("mapper");
        }
    }

    public static class Reduce extends Reducer<String, Integer, String, Integer> {
        public static Record<String, Integer> out;
        public void reduce(String key, Iterator<Integer> values, Record<String, Integer> output) throws IOException {
            int sum = 0;
            while (values.hasNext()) {
                sum += values.next();
            }
            output.set(key, sum);
        }

        public void print(){
            System.out.println("reducer");
        }
    }



}
