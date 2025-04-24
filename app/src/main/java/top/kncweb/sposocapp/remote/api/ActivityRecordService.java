package top.kncweb.sposocapp.remote.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.remote.models.UniResponse;

public interface ActivityRecordService {
    @Multipart
    @POST("activityrecord")
    Call<UniResponse<Long>> uploadActivityRecord(
            @Part("uid") long uid,
            @Part("rtype") RequestBody rtype,
            @Part("distance") float distance,
            @Part("rtime_start") RequestBody rtimeStart,
            @Part("rtime_end") RequestBody rtimeEnd,
            @Part MultipartBody.Part file
            );

    @GET("activityrecord/{rid}")
    Call<UniResponse<ActivityRecord>> getActivityRecord(@Path("rid") long rid);
}
