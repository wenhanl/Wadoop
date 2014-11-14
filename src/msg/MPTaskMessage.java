package msg;
import static msg.MPMessage.MessageType.*;
import mapr.Task;

/**
 * Created by CGJ on 14-11-13.
 */
public class MPTaskMessage extends MPMessage {
    private Task task;

    public MPTaskMessage(Task task) {
        super(TASK, task);
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
}