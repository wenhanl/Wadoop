package config;

/**
 * Created by wenhanl on 14-11-3.
 */
public class Config {
    // Number of replica per data block
    public static final int NUM_WHFS_REPLICA = 3;

    // Number of lines per block
    public static final int NUM_BLOCK_SIZE = 1000;

    // Mapper slot per machine
    public static final int NUM_MAP_SLOT = 5;

    // Reducer slot per machine
    public static final int NUM_REDUCE_SLOT = 1;

    // WHFS namenode port
    public static final int NAMENODE_PORT = 15440;

    // Heartbeat timeout
    public static final int HEARTBEAT_TIMEOUT = 10000;
}
