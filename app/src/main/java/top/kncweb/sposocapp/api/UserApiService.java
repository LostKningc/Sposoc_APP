package top.kncweb.sposocapp.api;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import top.kncweb.sposocapp.models.UniResponse;
import top.kncweb.sposocapp.models.User;
import top.kncweb.sposocapp.models.UserLogin;

public interface UserApiService {
    @POST("user")
    Call<UniResponse<Integer>> register(@Body User user);

    @FormUrlEncoded
    @POST("login")
    Call<UniResponse<String>> login(@Field("username") String username,
                                    @Field("password") String password);
}
