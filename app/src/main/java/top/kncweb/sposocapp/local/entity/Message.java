package top.kncweb.sposocapp.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import top.kncweb.sposocapp.enums.MessageType;

@Entity(tableName = "message")
public class Message {

    @PrimaryKey
    private long mid;
    private long senderUid;
    private long receiverUid;
    private String content;
    private MessageType type;
    private String sent_time;

    public long getMid() {
        return mid;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public long getSenderUid() {
        return senderUid;
    }

    public void setSenderUid(long senderUid) {
        this.senderUid = senderUid;
    }

    public long getReceiverUid() {
        return receiverUid;
    }

    public void setReceiverUid(long receiverUid) {
        this.receiverUid = receiverUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getSent_time() {
        return sent_time;
    }

    public void setSent_time(String sent_time) {
        this.sent_time = sent_time;
    }
}
