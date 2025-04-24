package top.kncweb.sposocapp.remote.models;

import java.io.Serializable;

public class Post implements Serializable {
    private long pid;

    private long uid;

    private long rid;

    String content;

    String time;

    Boolean friend_only;

    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getRid() {
        return rid;
    }

    public void setRid(long rid) {
        this.rid = rid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getFriend_only() {
        return friend_only;
    }

    public void setFriend_only(Boolean friend_only) {
        this.friend_only = friend_only;
    }
}
