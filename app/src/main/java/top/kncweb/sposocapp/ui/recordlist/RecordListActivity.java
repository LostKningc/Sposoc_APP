package top.kncweb.sposocapp.ui.recordlist;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.ActivityRecordDao;
import top.kncweb.sposocapp.local.entity.ActivityRecord;

public class RecordListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecordListAdapter adapter;
    private ActivityRecordDao dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_record_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bt_toMsg), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new RecordListAdapter();
        recyclerView.setAdapter(adapter);

        AppDatabase db = AppDatabase.getInstance(this);
        dao = db.activityRecordDao();

        new Thread(() -> {
            List<ActivityRecord> records = dao.getAllRecords();
            runOnUiThread(() -> adapter.setData(records));
        }).start();
    }
}