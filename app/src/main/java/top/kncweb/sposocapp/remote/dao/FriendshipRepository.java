package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.enums.FriendStatus;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.api.FriendshipService;
import top.kncweb.sposocapp.remote.models.Friendship;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public class FriendshipRepository {
    private final FriendshipService friendshipService;

    public FriendshipRepository(String token) {
        this.friendshipService = ApiClient.getFriendshipService(token);
    }

    public void getFriends(long uid , SCallback<List<User>> sCallback) {
        // Call the service to get friends
        friendshipService.getFriends(uid).enqueue(new Callback<UniResponse<List<User>>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<List<User>>> call, @NonNull Response<UniResponse<List<User>>> response) {
                UniResponse<List<User>> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<List<User>>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void addFriend(long uid, long fuid, SCallback<Friendship> sCallback) {
        // Call the service to add a friend
        Friendship friendship = new Friendship(uid, fuid, FriendStatus.pending);
        friendshipService.addFriend(friendship).enqueue(new Callback<UniResponse<Friendship>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Friendship>> call, @NonNull Response<UniResponse<Friendship>> response) {
                UniResponse<Friendship> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<Friendship>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }

    public void acceptFriend(long uid, long fuid, SCallback<Long> sCallback) {
        // Call the service to accept a friend
        friendshipService.acceptFriend(uid, fuid).enqueue(new Callback<UniResponse<Long>>() {
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

    public void rejectFriend(long uid, long fuid, SCallback<Long> sCallback) {
        // Call the service to reject a friend
        friendshipService.rejectFriend(uid, fuid).enqueue(new Callback<UniResponse<Long>>() {
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

    public void getFriendRequests(long uid, SCallback<List<User>> sCallback) {
        // Call the service to get friend requests
        friendshipService.getFriendRequests(uid).enqueue(new Callback<UniResponse<List<User>>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<List<User>>> call, @NonNull Response<UniResponse<List<User>>> response) {
                UniResponse<List<User>> apiResponse = response.body();
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
            public void onFailure(@NonNull Call<UniResponse<List<User>>> call, @NonNull Throwable t) {
                sCallback.onFailure(t.getMessage());
            }
        });
    }
}
