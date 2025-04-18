package top.kncweb.sposocapp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.ActivityRecordDao;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.enums.ActivityType;

public class TrackingActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private List<Location> locationList = new ArrayList<>();

    private boolean isTracking = false;
    private long startTime;
    private AppDatabase db;
    private ActivityRecordDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tracking);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        db = AppDatabase.getInstance(this);
        dao = db.activityRecordDao();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        findViewById(R.id.bt_start).setOnClickListener(v-> startTracking());
        findViewById(R.id.bt_end).setOnClickListener(v-> stopTracking());

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        locationList.add(location);
                    }
                }
                Log.d("Location", "位置更新: " + locationList.size() + " 个位置点");
            }
        };

    }

    private void startTracking() {
        if (isTracking) return;

        startTime = System.currentTimeMillis();
        isTracking = true;

        LocationRequest locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(2000)
                .build();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    1);
            isTracking = false;
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        Toast.makeText(this, "开始记录位置", Toast.LENGTH_SHORT).show();
        Log.d("Location", "开始记录位置");
    }

    private void stopTracking() {
        if (!isTracking) return;
        isTracking = false;
        fusedLocationClient.removeLocationUpdates(locationCallback);
        long endTime = System.currentTimeMillis();
        float distance = calculateDistance(locationList);

        if (locationList.isEmpty()) {
            Toast.makeText(this, "没有位置数据", Toast.LENGTH_SHORT).show();
            return;
        }

        String json = new Gson().toJson(locationList);
        File file = new File(getFilesDir(), "track_" + startTime + ".json");
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(json);
        } catch (IOException e) {
            Log.e("FileWrite", "写入文件失败: " + e.getMessage());
            Toast.makeText(this, "保存失败，请重试", Toast.LENGTH_SHORT).show();
        }

        ActivityRecord record = new ActivityRecord();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        record.setUid(1000001); // TODO: Replace with actual user ID
        record.setRtype(ActivityType.run); // TODO: Replace with actual activity type
        record.setRtime_start(sdf.format(new Date(startTime)));
        record.setRtime_end(sdf.format(new Date(endTime)));
        record.setDistance(distance);
        record.setGps_path(file.getAbsolutePath());
        record.setOnCloud(false); // TODO: Replace with actual cloud status

        new Thread(() -> dao.insert(record)).start();


    }

    private float calculateDistance(List<Location> list) {
        float distance = 0f;
        for (int i = 1; i < list.size(); i++) {
            distance += list.get(i - 1).distanceTo(list.get(i));
        }
        return distance;
    }
}