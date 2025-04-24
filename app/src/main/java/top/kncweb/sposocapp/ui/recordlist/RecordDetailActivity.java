package top.kncweb.sposocapp.ui.recordlist;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.ActivityRecordDao;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.remote.dao.ActivityRecordRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.util.JwtManager;

public class RecordDetailActivity extends AppCompatActivity {

    private ActivityRecordDao dao;

    private TextView tvTitle, tbRid, tbUid, tbRtype, tbGpsPath, tbDistance, tbRtimeStart, tbRtimeEnd, tbOnCloud;

    private ActivityRecord record;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bt_toMsg), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        long recordId = getIntent().getLongExtra("record_id", -1);
        if (recordId == -1) {
            Toast.makeText(this, "未找到记录 ID", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        dao = AppDatabase.getInstance(this).activityRecordDao();

        // 初始化控件
        tvTitle = findViewById(R.id.tv_title);
        tbRid = findViewById(R.id.tb_rid);
        tbUid = findViewById(R.id.tb_uid);
        tbRtype = findViewById(R.id.tb_rtype);
        tbGpsPath = findViewById(R.id.tb_gps_path);
        tbDistance = findViewById(R.id.tb_distance);
        tbRtimeStart = findViewById(R.id.tb_rtime_start);
        tbRtimeEnd = findViewById(R.id.tb_rtime_end);
        tbOnCloud = findViewById(R.id.tb_onCloud);
        Button btUpload = findViewById(R.id.bt_upload);

        // 启动线程读取数据
        new Thread(() -> {
            record = dao.getRecordById(recordId);

            if (record != null) {
                runOnUiThread(() -> {
                    tvTitle.setText("记录详情");
                    tbRid.setText("记录ID: " + record.getId());
                    tbUid.setText("用户ID: " + record.getUid());
                    tbRtype.setText("类型: " + record.getRtype());
                    tbGpsPath.setText("GPS路径: " + record.getGps_path());
                    tbDistance.setText("距离: " + record.getDistance() + " 米");
                    tbRtimeStart.setText("开始时间: " + record.getRtime_start());
                    tbRtimeEnd.setText("结束时间: " + record.getRtime_end());
                    tbOnCloud.setText("是否在云端: " + (record.isOnCloud() ? "是" : "否"));
                    btUpload.setVisibility(record.isOnCloud() ? View.GONE : View.VISIBLE);
                });
            } else {
                runOnUiThread(() ->
                        Toast.makeText(RecordDetailActivity.this, "未找到该记录", Toast.LENGTH_SHORT).show());
            }
        }).start();

        btUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (record == null) {
                    Toast.makeText(RecordDetailActivity.this, "记录不存在", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (record.isOnCloud()) {
                    Toast.makeText(RecordDetailActivity.this, "记录已在云端", Toast.LENGTH_SHORT).show();
                    return;
                }
                ActivityRecordRepository activityRecordRepository = new ActivityRecordRepository(JwtManager.getJwtToken(RecordDetailActivity.this));
                activityRecordRepository.uploadActivityRecord(
                                record.getUid(),
                                record.getRtype().toString(),
                                record.getDistance(),
                                record.getRtime_start(),
                                record.getRtime_end(),
                                record.getGps_path(),
                        new SCallback<Long>() {
                            @Override
                            public void onSuccess(Long rid) {
                                Toast.makeText(RecordDetailActivity.this, "上传成功，记录ID: " + rid, Toast.LENGTH_SHORT).show();
                                // 更新本地数据库
                                new Thread(() -> {
                                    record.setOnCloud(true);
                                    tbOnCloud.setText("是否在云端: 是");
                                    dao.updateOnCloudStatus(record.getId(), true);
                                }).start();
                            }

                            @Override
                            public void onFailure(String msg) {
                                Toast.makeText(RecordDetailActivity.this, "上传失败: " + msg, Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}