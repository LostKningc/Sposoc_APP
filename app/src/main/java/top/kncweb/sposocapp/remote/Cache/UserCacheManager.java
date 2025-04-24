package top.kncweb.sposocapp.remote.Cache;

import android.content.Context;

import java.util.HashMap;
import java.util.Map;

import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserRepository;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class UserCacheManager {
    private static final Map<Long, String> userMap = new HashMap<>();
    private static UserRepository userRepository;

    public static void init(Context context) {
        userRepository = new UserRepository(JwtManager.getJwtToken(context));
    }

    public static void put(Long uid, String name) {
        userMap.put(uid, name);
    }

    public static String get(Long uid, Context context, SCallback<String> callback) {
        if (userMap.containsKey(uid)) {
            return userMap.get(uid);
        } else {
            // 缓存 miss，去网络请求
            if (userRepository == null) {
                userRepository = new UserRepository(JwtManager.getJwtToken(context));
            }
            userRepository.getUser(uid, new SCallback<User>() {
                @Override
                public void onSuccess(User user) {
                    String name = user.getName();
                    userMap.put(uid, name);
                    callback.onSuccess(name);
                }

                @Override
                public void onFailure(String msg) {
                    callback.onFailure(msg);
                }
            });

        }
        return null;
    }

}
