package top.kncweb.sposocapp.remote.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import top.kncweb.sposocapp.remote.models.Comment;
import top.kncweb.sposocapp.remote.models.UniResponse;

public interface CommentApiService {
    @GET("post/{pid}/comment")
    Call<UniResponse<List<Comment>>> getComments(@Path("pid") long pid);

    @GET("post/{pid}/commentnum")
    Call<UniResponse<Long>> getCommentNum(@Path("pid") long pid);

    @POST("comment")
    Call<UniResponse<Long>> addComment(@Body Comment comment);
}
