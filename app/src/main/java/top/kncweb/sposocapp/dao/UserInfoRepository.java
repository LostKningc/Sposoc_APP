package top.kncweb.sposocapp.dao;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.api.ApiClient;
import top.kncweb.sposocapp.api.UserInfoApiService;
import top.kncweb.sposocapp.models.UniResponse;
import top.kncweb.sposocapp.models.UserInfo;

public class UserInfoRepository {
    private UserInfoApiService userInfoApiService;

    public UserInfoRepository(String token){
        this.userInfoApiService = ApiClient.getUserInfoApiService(token);
    }

    public void getUserInfo(int uid, final SCallback<UserInfo> sCallback){
        userInfoApiService.getUserInfo(uid).enqueue(new Callback<UniResponse<UserInfo>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<UserInfo>> call, @NonNull Response<UniResponse<UserInfo>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    UniResponse<UserInfo> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<UserInfo>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void addUserInfo(UserInfo userInfo, final SCallback<Integer> sCallback){
        userInfoApiService.addUserInfo(userInfo).enqueue(new Callback<UniResponse<Integer>>() {
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

    public void updateUserInfo(int id, Integer height, Integer weight, String sex, final SCallback<Boolean> sCallback) {
        userInfoApiService.updateUserInfo(id, height, weight, sex).enqueue(new Callback<UniResponse<Boolean>>() {
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
