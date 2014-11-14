package mapr;

/**
 * Created by CGJ on 14-11-13.
 */
public class MapperTask extends Task {
    private Mapper mapper;
    String hostname;

    public MapperTask(Mapper mapper, String hostname, int taskID, String input) {
        super(taskID, input);
        this.mapper = mapper;
        this.hostname = hostname;
    }

    public Mapper getMapper() {
        return mapper;
    }

    public String getHostname() { return hostname; }

}
