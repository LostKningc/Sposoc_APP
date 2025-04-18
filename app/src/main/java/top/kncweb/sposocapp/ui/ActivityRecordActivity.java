package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.ui.recordlist.RecordListActivity;

public class ActivityRecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_activity_record);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button toTrackingButton = findViewById(R.id.bt_toTrack);
        toTrackingButton.setOnClickListener(v -> {
            // Start the TrackingActivity
            startActivity(new Intent(this, TrackingActivity.class));
        });

        Button toRecordListButton = findViewById(R.id.bt_toList);
        toRecordListButton.setOnClickListener(v -> {
            // Start the RecordListActivity
            startActivity(new Intent(this, RecordListActivity.class));
        });
    }

}