package top.kncweb.sposocapp.remote.dao;


public interface SCallback<T> {
    void onSuccess(T uid);
    void onFailure(String msg);
}
