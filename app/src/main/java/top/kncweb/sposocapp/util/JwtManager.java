package top.kncweb.sposocapp.util;

import android.content.Context;

public class JwtManager {
    private static String jwtToken;
    private static long globalUid = -1;

    public static boolean isLoggedIn(Context context) {
        return getJwtToken(context) != null;
    }

    public static String getJwtToken(Context context) {
        if (jwtToken == null) {
            jwtToken = new EncryptedSharedPreferencesHelper(context).getJwtToken();
        }
        return jwtToken;
    }

    public static void setJwtToken(Context context, String token) {
        jwtToken = token;
        new EncryptedSharedPreferencesHelper(context).saveJwtToken(token);
    }


    public static void clearJwtToken(Context context) {
        jwtToken = null;
        new EncryptedSharedPreferencesHelper(context).deleteJwtToken();
    }

    public static long getGlobalUid(Context context) {
        if (globalUid == -1) {
            globalUid = new EncryptedSharedPreferencesHelper(context).getGlobalUid();
        }
        return globalUid;
    }

    public static void setGlobalUid(Context context, long uid) {
        globalUid = uid;
        new EncryptedSharedPreferencesHelper(context).saveGlobalUid(uid);
    }
}
