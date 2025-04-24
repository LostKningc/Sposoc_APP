package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.LikeApiService;
import top.kncweb.sposocapp.remote.models.PostLike;
import top.kncweb.sposocapp.remote.models.UniResponse;

public class LikeRepository {
    private final LikeApiService likeApiService;
    public LikeRepository(String token) {
        this.likeApiService = ApiClient.getLikeApiService(token);
    }

    public void addLike(long uid, long pid, SCallback<Long> sCallback) {
        PostLike postLike = new PostLike();
        postLike.setUid(uid);
        postLike.setPid(pid);
        likeApiService.addLike(postLike).enqueue(new Callback<UniResponse<Long>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Long>> call, @NonNull Response<UniResponse<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<Long> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Long>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void getLikeNum(long pid, SCallback<Long> sCallback) {
        likeApiService.getLikeNum(pid).enqueue(new Callback<UniResponse<Long>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Long>> call, @NonNull Response<UniResponse<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<Long> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Long>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void deleteLike(long uid, long pid, SCallback<Boolean> sCallback) {
        likeApiService.deleteLike(uid, pid).enqueue(new Callback<UniResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Boolean>> call, @NonNull Response<UniResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<Boolean> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Boolean>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void isLiked(long pid, SCallback<Boolean> sCallback) {
        likeApiService.isLiked(pid).enqueue(new Callback<UniResponse<Boolean>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Boolean>> call, @NonNull Response<UniResponse<Boolean>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<Boolean> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Boolean>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }
}
