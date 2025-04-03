package top.kncweb.sposocapp.util;

import android.content.Context;

public class JwtManager {
    private static String jwtToken;

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
}
