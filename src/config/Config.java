package config;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Config {
    // Number of replica per data block
    public static final int NUM_WHFS_REPLICA = 3;

    // Number of lines per block
    public static final int BLOCK_SIZE = 100;

    // Mapper slot per machine
    public static final int NUM_MAP_SLOT = 5;

    // Reducer slot per machine
    public static final int NUM_REDUCE_SLOT = 1;

    // WHFS namenode port
    public static final int NAMENODE_PORT = 15440;

    // Heartbeat timeout in ms
    public static final int HEARTBEAT_TIMEOUT = 10000;

    // DataNode file server port
    public static final int DATANODE_FILE_PORT = 15441;

    // Local base path
    public static final String LOCAL_BASE_PATH = "/tmp/640local/";

    // WHFS base path
    public static final String WHFS_BASE_PATH = "/tmp/whfs/";
}
