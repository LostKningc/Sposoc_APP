package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.AuthApiService;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public class AuthRepository {
    private AuthApiService authApiService;

    public AuthRepository(){
        this.authApiService = ApiClient.getUserApiService();
    }

    public void addUser(User user, final SCallback<Integer> sCallback) {
        authApiService.register(user).enqueue(new Callback<UniResponse<Integer>>() {
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
        authApiService.login(username, password).enqueue(new Callback<UniResponse<String>>() {
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

    public void validate(String token, final SCallback<Boolean> sCallback){
        authApiService.validate(token).enqueue(new Callback<UniResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Boolean>> call, @NonNull Response<UniResponse<Boolean>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    UniResponse<Boolean> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Boolean>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }
}
