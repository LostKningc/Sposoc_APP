package top.kncweb.sposocapp.remote.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import top.kncweb.sposocapp.remote.models.Post;
import top.kncweb.sposocapp.remote.models.UniResponse;

public interface PostApiService {
    @GET("post")
    Call<UniResponse<List<Post>>> getPosts(@Query("uid") long uid);

    @GET("post/{pid}")
    Call<UniResponse<Post>> getPostById(@Path("pid") long pid);

    @POST("post")
    Call<UniResponse<Long>> addPost(@Body Post post);
}
