package top.kncweb.sposocapp.remote.models;

import java.io.Serializable;

public class UserInfo implements Serializable {

    private long uid;
    private int height;
    private int weight;
    private String sex;

    public UserInfo(long uid, int height, int weight, String sex) {
        this.uid = uid;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
