package top.kncweb.sposocapp.remote.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import top.kncweb.sposocapp.remote.models.PostLike;
import top.kncweb.sposocapp.remote.models.UniResponse;

public interface LikeApiService {
    @POST("post/likes")
    Call<UniResponse<Long>> addLike(@Body PostLike postLike);

    @GET("post/{pid}/likenum")
    Call<UniResponse<Long>> getLikeNum(@Path("pid") long pid);

    @DELETE("/post/likes")
    Call<UniResponse<Boolean>> deleteLike(@Query("uid") long uid,
                                       @Query("pid") long pid);
    @GET("post/{pid}/like")
    Call<UniResponse<Boolean>> isLiked(@Path("pid") long pid);
}
