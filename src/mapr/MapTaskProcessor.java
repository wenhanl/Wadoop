package mapr;

import config.Config;
import msg.MPMessageManager;
import msg.TaskUpdateMessage;

import java.io.*;
import java.util.List;

/**
 * Created by CGJ on 14-11-13.
 */
public class MapTaskProcessor extends Thread {
    private MapperTask task;

    public MapTaskProcessor(MapperTask mapperTask, MPMessageManager commHandler) {
        this.task = mapperTask;
    }

    public void run() {
        Mapper mapper = task.getMapper();

        File baseDir = new File(Config.WHFS_BASE_PATH + task.getHostname() + "/");
        File[] fileList = baseDir.listFiles();
        for (File file : fileList) {
            String path = file.getAbsolutePath();
            if (path.contains(task.getInput())) {
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
                    String line;
                    int lineNum = 0;
                    while ((line = br.readLine()) != null) {
                        mapper.map(lineNum++, line);
                    }
                    br.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        //write mapper result into tmp/mapreduce/
        List<Record> mapOutput = mapper.getMapOutput();
        String resultFileName = Config.MAP_RESULTS_FOLDER + "MapTempFile_" + task.getInput();
        File outputFile = new File(resultFileName);
        try {
            FileWriter fw = new FileWriter(outputFile);
            for (Record record : mapOutput) {
                fw.write(record.getKey() + "\t" + record.getValue() + "\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // We are DONE, TELL MASTER we are done
        try {
            MPMessageManager mpm = new MPMessageManager(Config.MASTER_NODE, Config.DATA_PORT);
            mpm.sendMessage(new TaskUpdateMessage(task.getTaskID(), false, true, task.getInput()));
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
