package top.kncweb.sposocapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.enums.ActivityType;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.ActivityRecordDao;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.ui.TrackingActivity;
import top.kncweb.sposocapp.ui.recordlist.RecordListAdapter;

public class SportFragment extends Fragment {

    private LinearLayout btnRun, btnBike, btnWalk;
    private RecyclerView rvHistory;
    private RecordListAdapter recordListAdapter;
    private ActivityRecordDao dao;

    public SportFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sport, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化视图
        btnRun = view.findViewById(R.id.ll_running);
        btnBike = view.findViewById(R.id.ll_cycling);
        btnWalk = view.findViewById(R.id.ll_walking);
        rvHistory = view.findViewById(R.id.rv_history);

        // 设置按钮点击事件
        btnRun.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TrackingActivity.class);
            intent.putExtra("activityType", ActivityType.run);
            startActivity(intent);
        });

        btnBike.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TrackingActivity.class);
            intent.putExtra("activityType", ActivityType.cycling);
            startActivity(intent);
        });

        btnWalk.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), TrackingActivity.class);
            intent.putExtra("activityType", ActivityType.walk);
            startActivity(intent);
        });

        AppDatabase db = AppDatabase.getInstance(SposocApplication.getContext());
        dao = db.activityRecordDao();
        // 初始化历史记录列表
        recordListAdapter = new RecordListAdapter();
        rvHistory.setLayoutManager(new LinearLayoutManager(getContext()));
        rvHistory.setAdapter(recordListAdapter);

        new Thread(() -> {
            List<ActivityRecord> records = dao.getAllRecords();
            if(isAdded()) {
                requireActivity().runOnUiThread(() -> recordListAdapter.setData(records));
            }
        }).start();
    }
}
