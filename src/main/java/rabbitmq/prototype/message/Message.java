package rabbitmq.prototype.message;

/**
 * Created by tharder on 10/04/15.
 */
public class Message<T> {
    public enum Action {
        CREATED,
        UPDATED,
        DELETED
    };

    public final String type;
    public final Action action;
    public final String payload;
    public final long timestamp;
    public final String tId;
    public final long uId;


    Message(String type, Action action, String tId, long uId, String payload, long timestamp) {
        this.type = type;
        this.action = action;
        this.payload = payload;
        this.tId = tId;
        this.uId = uId;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return String.format("Type: %s, Action: %s, T: %s, U: %d, Timestamp: %d", this.type, this.action, this.tId, this.uId, this.timestamp);
    }
}
