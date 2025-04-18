package top.kncweb.sposocapp.remote.api;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static final String BASE_URL = "http://172.18.0.101:8080/";
    private static Retrofit retrofit = null;
    private static String currentToken = null;

    public static Retrofit getClient(String token) {
        if (retrofit == null || !token.equals(currentToken)) {  // 当 token 变更时重新创建 Retrofit
            currentToken = token;
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor(token))
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

    public static UserApiService getUserApiService(){
        return getClient("").create(UserApiService.class);
    }

    public static UserInfoApiService getUserInfoApiService(String token) {
        return getClient(token).create(UserInfoApiService.class);
    }

    public static ActivityRecordService getActivityRecordService(String token) {
        return getClient(token).create(ActivityRecordService.class);
    }
}
