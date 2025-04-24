package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.CommentApiService;
import top.kncweb.sposocapp.remote.models.Comment;
import top.kncweb.sposocapp.remote.models.UniResponse;

public class CommentRepository {

    private final CommentApiService commentApiService;

    public CommentRepository(String token) {
        this.commentApiService = ApiClient.getCommentApiService(token);
    }

    public void getComments(long pid, SCallback<List<Comment>> sCallback) {
        commentApiService.getComments(pid).enqueue(new Callback<UniResponse<List<Comment>>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<List<Comment>>> call, @NonNull Response<UniResponse<List<Comment>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<List<Comment>> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<List<Comment>>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void getCommentNum(long pid, SCallback<Long> sCallback) {
        commentApiService.getCommentNum(pid).enqueue(new Callback<UniResponse<Long>>() {
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

    public void addComment(Comment comment, SCallback<Long> sCallback) {
        commentApiService.addComment(comment).enqueue(new Callback<UniResponse<Long>>() {
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
}
