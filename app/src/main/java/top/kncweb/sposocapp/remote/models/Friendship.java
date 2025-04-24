package top.kncweb.sposocapp.remote.models;

import top.kncweb.sposocapp.enums.FriendStatus;

public class Friendship {
    private long fid;

    private long uid;

    private long fuid;

    private FriendStatus status;

    public Friendship(long uid, long fuid, FriendStatus status) {
        this.uid = uid;
        this.fuid = fuid;
        this.status = status;
    }

    public Friendship reverse() {
        return new Friendship(fuid, uid, status);
    }

    public long getFid() {
        return fid;
    }

    public void setFid(long fid) {
        this.fid = fid;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public long getFuid() {
        return fuid;
    }

    public void setFuid(long fuid) {
        this.fuid = fuid;
    }

    public FriendStatus getStatus() {
        return status;
    }

    public void setStatus(FriendStatus status) {
        this.status = status;
    }
}
