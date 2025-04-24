package top.kncweb.sposocapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.api.WebSocketClient;
import top.kncweb.sposocapp.util.JwtManager;

public class SposocApplication extends Application {

    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        String jwt = JwtManager.getJwtToken(this);
        if (jwt != null) {
            WebSocketClient.getInstance().connect(jwt);
        }
        UserCacheManager.init(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        WebSocketClient.getInstance().close();
    }

    public static Context getContext() {
        return context;
    }
}
