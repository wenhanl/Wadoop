package msg;

/**
 * Created by CGJ on 14-11-13.
 */
import java.io.Serializable;
import java.util.Objects;

public abstract class MPMessage implements Serializable {

    public static enum MessageType {
        TASK,
        PARTITION,
        UPDATE,
    }

    private final MessageType type;
    protected final Object payload;

    MPMessage(MessageType type, Object payload) {
        this.type = type;
        this.payload = payload;
    }

    public MessageType getType() {
        return type;
    }


}