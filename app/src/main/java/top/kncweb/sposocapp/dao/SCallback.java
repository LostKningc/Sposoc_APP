package top.kncweb.sposocapp.dao;


public interface SCallback<T> {
    void onSuccess(T uid);
    void onFailure(String msg);
}
