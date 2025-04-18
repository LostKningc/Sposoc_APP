package top.kncweb.sposocapp.remote.models;

public class UserInfo {

    private int uid;
    private int height;
    private int weight;
    private String sex;

    public UserInfo(int uid, int height, int weight, String sex) {
        this.uid = uid;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
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
