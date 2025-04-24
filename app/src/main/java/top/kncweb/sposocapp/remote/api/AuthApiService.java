package top.kncweb.sposocapp.remote.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public interface AuthApiService {
    @POST("user")
    Call<UniResponse<Integer>> register(@Body User user);

    @FormUrlEncoded
    @POST("login")
    Call<UniResponse<String>> login(@Field("username") String username,
                                    @Field("password") String password);

    @FormUrlEncoded
    @POST("/login/validate")
    Call<UniResponse<Boolean>> validate(@Field("token") String token);
}
