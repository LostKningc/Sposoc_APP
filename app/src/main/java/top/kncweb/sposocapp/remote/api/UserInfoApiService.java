package top.kncweb.sposocapp.remote.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.UserInfo;

public interface UserInfoApiService {

    @GET("userinfo/{id}")
    Call<UniResponse<UserInfo>> getUserInfo(@Path("id") long uid);

    @POST("userinfo")
    Call<UniResponse<Integer>> addUserInfo(@Body UserInfo userInfo);

    @FormUrlEncoded
    @PUT("userinfo/{id}")
    Call<UniResponse<Boolean>> updateUserInfo(@Path("id") long id,
                                              @Field("height") Integer height,  // 使用 Integer 允许 null
                                              @Field("weight") Integer weight,
                                              @Field("sex") String sex);


}
