package top.kncweb.sposocapp.remote.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import top.kncweb.sposocapp.remote.models.UniResponse;
import top.kncweb.sposocapp.remote.models.User;

public interface UserApiService {
    @GET("user/{uid}")
    Call<UniResponse<User>> getUser(@Path("uid") long uid);

}
