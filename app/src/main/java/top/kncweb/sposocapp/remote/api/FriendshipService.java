package top.kncweb.sposocapp.remote.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import top.kncweb.sposocapp.remote.models.Friendship;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public interface FriendshipService {
    @GET("friends/{uid}")
    Call<UniResponse<List<User>>> getFriends(@Path("uid") long uid);

    @POST("friends/add")
    Call<UniResponse<Friendship>> addFriend(@Body Friendship friendship);

    @FormUrlEncoded
    @POST("/friends/accept")
    Call<UniResponse<Long>> acceptFriend(@Field("uid") long uid,
                                               @Field("fuid") long fuid);

    @FormUrlEncoded
    @POST("/friends/reject")
    Call<UniResponse<Long>> rejectFriend(@Field("uid") long uid,
                                               @Field("fuid") long fuid);

    @GET("friends/requests/{uid}")
    Call<UniResponse<List<User>>> getFriendRequests(@Path("uid") long uid);
}
