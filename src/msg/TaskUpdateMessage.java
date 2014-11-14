package msg;

import static msg.MPMessage.MessageType.*;

/**
 * Created by CGJ on 14-11-13.
 */

public class TaskUpdateMessage extends MPMessage {
    private int taskID;
    private boolean running;
    private boolean done;
    String input;

    public TaskUpdateMessage(int taskID, boolean running, boolean done, String input) {
        super(UPDATE, taskID);
        this.taskID = taskID;
        this.running = running;
        this.done = done;
        this.input = input;
    }

    public TaskUpdateMessage(int taskID, boolean running, boolean done, Object payload, String input) {
        super(UPDATE, payload);
        this.taskID = taskID;
        this.running = running;
        this.done = done;
        this.input = input;
    }

    public int getTaskID() {
        return taskID;
    }

    public boolean isRunning() {
        return running;
    }

    public boolean isDone() {
        return done;
    }

    public Object getPayload() {
        return payload;
    }

    public String getInput() { return input; }


}
