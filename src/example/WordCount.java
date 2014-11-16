package example;

import mapr.MapReduceJob;
import mapr.Mapper;
import mapr.Record;
import mapr.Reducer;

import java.util.List;

/**
 * Created by CGJ on 14-11-12.
 */

public class WordCount extends MapReduceJob<Integer, String, String, String> {

    public Mapper<Integer, String, String, String> getMapper() {
        return new Mapper<Integer, String, String, String>() {
            //map each key to "1"
            public void map(Integer key, String value) {
                String[] words = value.replaceAll("[^a-zA-Z ]", "").toLowerCase().split(" ");
                for (String keyWord : words) {
                    if(keyWord.length()!=0)
                        output.add(new Record<String, String>(keyWord.trim(), "1"));
                }
            }
        };
    }

    public Reducer<String, String, String, String> getReducer() {
        return new Reducer<String, String, String, String>() {
            //the value of the same key is put in a list
            public void reduce(String key, List<String> values) {
                Integer sum = 0;
                for (String value : values) {
                    sum += Integer.parseInt(value);
                }
                output.add(new Record<String, String>(key, sum.toString()));
            }
        };
    }
}
