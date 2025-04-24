package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.UserApiService;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public class UserRepository {
    private UserApiService userApiService;

    public UserRepository(String token) {
        this.userApiService = ApiClient.getUserApiService(token);
    }

    public void getUser(long uid, final SCallback<User> sCallback) {
        userApiService.getUser(uid).enqueue(new Callback<UniResponse<User>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<User>> call, @NonNull Response<UniResponse<User>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<User> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse.getMessage());
                    }
                } else {
                    sCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniResponse<User>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }
}
