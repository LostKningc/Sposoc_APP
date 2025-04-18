package top.kncweb.sposocapp.remote.models;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {

    String uid;

    String name;

    String password;

    public User(String name, String rawPassword, String uid){
        this.uid = uid;
        this.name = name;
        this.password = sha256(rawPassword);
    }

    private static String sha256(String rawPassword){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rawPassword.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 Algorithm not found", e);
        }
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
}
