package config;

/**
 * Created by CGJ on 14-11-13.
 */
public class Config {


    public static final String MASTER_NODE = "unix1.andrew.cmu.edu";
//    // SlaveNodes info
    public static final String[] SLAVE_NODES = {
            "unix2.andrew.cmu.edu",  //"unix2.andrew.cmu.edu"
            "unix3.andrew.cmu.edu"
    };
    // port for transferring data in mapreduce phase
    public static final int DATA_PORT = 19782;

    // port for sending task message
    public static final int TASK_PORT = 18432;

    // Number of replica per data block
    public static final int NUM_WHFS_REPLICA = 2;

    // Number of lines per block
    public static final int BLOCK_SIZE = 1;

    // Mapper slot per machine
    public static final int NUM_MAP_SLOT = 5;

    // Reducer slot per machine
    public static final int NUM_REDUCE_SLOT = 1;

    // Number of reducers -- this number should less or equal to the number of DataNodes!
    public static final int NUM_REDUCERS = 1;

    // WHFS namenode port
    public static final int NAMENODE_PORT = 15440;

    // Heartbeat timeout in ms
    public static final int HEARTBEAT_TIMEOUT = 10000;

    // DataNode file server port
    public static final int DATANODE_FILE_PORT = 15441;

    // Local base path
    public static final String LOCAL_BASE_PATH = "/tmp/wenhanlocal/";

    // WHFS base path
//    public static final String WHFS_BASE_PATH = "/tmp/whfs/";

    // map and reduce result file folder
    public static final String MAP_RESULTS_FOLDER = "/tmp/mapreduce/";


    public static final String WHFS_BASE_PATH = "/tmp/wenhanwhfs/";

    // WHFS temp path
    public static final String WHFS_TEMP_PATH = "/tmp/wenhantemp";
}
