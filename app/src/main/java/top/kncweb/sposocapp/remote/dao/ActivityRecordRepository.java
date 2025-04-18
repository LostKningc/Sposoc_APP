package top.kncweb.sposocapp.remote.dao;

import androidx.annotation.NonNull;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.kncweb.sposocapp.remote.api.ActivityRecordService;
import top.kncweb.sposocapp.remote.api.ApiClient;
import top.kncweb.sposocapp.remote.models.UniResponse;

public class ActivityRecordRepository {
    private final ActivityRecordService activityRecordService;

    public ActivityRecordRepository(String token){
        this.activityRecordService = ApiClient.getActivityRecordService(token);
    }

    public void uploadActivityRecord(long uid,
                                     String rtype,
                                     float distance,
                                     String rtimeStart,
                                     String rtimeEnd,
                                     String file_path,
                                     SCallback<Long> sCallback) {

        File gps_file = new File(file_path);
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("application/json"), gps_file);
        MultipartBody.Part file = MultipartBody.Part.createFormData("file", gps_file.getName(), fileReqBody);

        RequestBody rtypeReqBody = RequestBody.create(MediaType.parse("text/plain"), rtype);
        RequestBody rtimeStartReqBody = RequestBody.create(MediaType.parse("text/plain"), rtimeStart);
        RequestBody rtimeEndReqBody = RequestBody.create(MediaType.parse("text/plain"), rtimeEnd);

        activityRecordService.uploadActivityRecord(uid, rtypeReqBody, distance, rtimeStartReqBody, rtimeEndReqBody, file).enqueue(new Callback<UniResponse<Long>>() {
            @Override
            public void onResponse(@NonNull Call<UniResponse<Long>> call, @NonNull Response<UniResponse<Long>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UniResponse<Long> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        sCallback.onSuccess(apiResponse.getData());
                    } else {
                        sCallback.onFailure(apiResponse.getMessage());
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
}
