package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.PostApiService;
import top.kncweb.sposocapp.remote.models.Post;
import top.kncweb.sposocapp.remote.models.UniResponse;

public class PostRepository {
    private final PostApiService postApiService;

    public PostRepository(String token) {
        this.postApiService = ApiClient.getPostApiService(token);
    }

    public void getPosts(long uid, SCallback<List<Post>> sCallback) {
        postApiService.getPosts(uid).enqueue(new Callback<UniResponse<List<Post>>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<List<Post>>> call, @NonNull Response<UniResponse<List<Post>>> response) {
                UniResponse<List<Post>> apiResponse = response.body();
                if (response.isSuccessful()) {
                    if(apiResponse != null && apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse != null ? apiResponse.getMessage() : "Unknown error");
                    }
                } else {
                    sCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniResponse<List<Post>>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void getPostById(long pid, SCallback<Post> sCallback) {
        postApiService.getPostById(pid).enqueue(new Callback<UniResponse<Post>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Post>> call, @NonNull Response<UniResponse<Post>> response) {
                UniResponse<Post> apiResponse = response.body();
                if (response.isSuccessful()) {
                    if(apiResponse != null && apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse != null ? apiResponse.getMessage() : "Unknown error");
                    }
                } else {
                    sCallback.onFailure(response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<UniResponse<Post>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void addPost(Post post, SCallback<Long> sCallback) {
        postApiService.addPost(post).enqueue(new Callback<UniResponse<Long>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Long>> call, @NonNull Response<UniResponse<Long>> response) {
                UniResponse<Long> apiResponse = response.body();
                if (response.isSuccessful()) {
                    if(apiResponse != null && apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse != null ? apiResponse.getMessage() : "Unknown error");
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
}
