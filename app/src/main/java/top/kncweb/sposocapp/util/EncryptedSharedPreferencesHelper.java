package top.kncweb.sposocapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class EncryptedSharedPreferencesHelper {

    private static final String PREF_NAME = "app_prefs"; // SharedPreferences 的名字
    private static final String JWT_KEY = "jwt_token"; // 用于存储 JWT 的 key
    private SharedPreferences encryptedSharedPreferences;

    // 构造函数，初始化 EncryptedSharedPreferences
    public EncryptedSharedPreferencesHelper(Context context) {
        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);

            // 初始化 EncryptedSharedPreferences
            encryptedSharedPreferences = EncryptedSharedPreferences.create(
                    PREF_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (Exception e) {
            Log.e("EncryptedPrefs", "Error initializing EncryptedSharedPreferences", e);
        }
    }

    // 存储 JWT
    public void saveJwtToken(String jwtToken) {
        try {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.putString(JWT_KEY, jwtToken);
            editor.apply(); // 提交更改
        } catch (Exception e) {
            Log.e("EncryptedPrefs", "Error saving JWT token", e);
        }
    }

    // 获取 JWT
    public String getJwtToken() {
        try {
            return encryptedSharedPreferences.getString(JWT_KEY, null);
        } catch (Exception e) {
            Log.e("EncryptedPrefs", "Error retrieving JWT token", e);
            return null;
        }
    }

    // 删除 JWT
    public void deleteJwtToken() {
        try {
            SharedPreferences.Editor editor = encryptedSharedPreferences.edit();
            editor.remove(JWT_KEY);
            editor.apply(); // 提交更改
        } catch (Exception e) {
            Log.e("EncryptedPrefs", "Error deleting JWT token", e);
        }
    }
}
