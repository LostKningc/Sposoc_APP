package top.kncweb.sposocapp.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import top.kncweb.sposocapp.enums.ActivityType;

@Entity(tableName = "activity_record")
public class ActivityRecord {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long uid;

    private ActivityType rtype;

    private String gps_path;

    private float distance;

    private String rtime_start;

    private String rtime_end;

    private boolean onCloud;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public ActivityType getRtype() {
        return rtype;
    }

    public void setRtype(ActivityType rtype) {
        this.rtype = rtype;
    }

    public String getGps_path() {
        return gps_path;
    }

    public void setGps_path(String gps_path) {
        this.gps_path = gps_path;
    }

    public String getRtime_start() {
        return rtime_start;
    }

    public void setRtime_start(String rtime_start) {
        this.rtime_start = rtime_start;
    }

    public String getRtime_end() {
        return rtime_end;
    }

    public void setRtime_end(String rtime_end) {
        this.rtime_end = rtime_end;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public boolean isOnCloud() {
        return onCloud;
    }

    public void setOnCloud(boolean onCloud) {
        this.onCloud = onCloud;
    }
}
