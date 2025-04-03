package top.kncweb.sposocapp.dao;

import android.util.Log;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.api.ApiClient;
import top.kncweb.sposocapp.api.UserApiService;
import top.kncweb.sposocapp.models.UniResponse;
import top.kncweb.sposocapp.models.User;

public class UserRepository {
    private UserApiService userApiService;

    public UserRepository(){
        this.userApiService = ApiClient.getUserApiService();
    }

    public void addUser(User user, final SCallback<Integer> sCallback) {
        userApiService.register(user).enqueue(new Callback<UniResponse<Integer>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Integer>> call, @NonNull Response<UniResponse<Integer>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    UniResponse<Integer> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse.getMessage());
                    }
                }
                else {
                    sCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniResponse<Integer>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void login(String username, String password, final SCallback<String> sCallback){
        userApiService.login(username, password).enqueue(new Callback<UniResponse<String>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<String>> call, @NonNull Response<UniResponse<String>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    UniResponse<String> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse.getMessage());
                    }
                }
                else {
                    sCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniResponse<String>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }
}
