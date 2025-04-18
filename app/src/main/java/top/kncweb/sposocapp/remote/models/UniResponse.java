package top.kncweb.sposocapp.remote.models;

public class UniResponse<T> {
    private T data;
    private boolean success;
    private String message;

    public T getData() { return data; }
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
}
