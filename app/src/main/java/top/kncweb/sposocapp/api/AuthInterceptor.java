package top.kncweb.sposocapp.api;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import top.kncweb.sposocapp.util.JwtManager;

public class AuthInterceptor implements Interceptor {
    private String jwtToken;

    public AuthInterceptor(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        Request newRequest = originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer " + jwtToken)
                .build();
        return chain.proceed(newRequest);
    }
}
