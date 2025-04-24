package top.kncweb.sposocapp.ui.MessageListActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.local.AppDatabase;
import top.kncweb.sposocapp.local.dao.MessageDao;
import top.kncweb.sposocapp.local.entity.Message;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserRepository;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class MessageListActivity extends AppCompatActivity {

    private MessageListAdapter messageListAdapter;
    private RecyclerView recyclerView;
    private View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_message_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.bt_toMsg), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化视图
        recyclerView = findViewById(R.id.messageRecyclerView);
        emptyView = findViewById(R.id.empty_view);

        // 设置RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        messageListAdapter = new MessageListAdapter();
        recyclerView.setAdapter(messageListAdapter);

        // 加载消息数据
        loadMessages();
    }

    private void loadMessages() {
        AppDatabase db = AppDatabase.getInstance(this);
        MessageDao messageDao = db.messageDao();

        new Thread(() -> {
            long userId = JwtManager.getGlobalUid(SposocApplication.getContext());
            List<Message> messages = messageDao.getLastMessagesPerUser(userId);

            // 在UI线程更新界面
            runOnUiThread(() -> updateUI(messages));
        }).start();
    }

    private void updateUI(List<Message> messages) {
        if (messages == null) {
            messages = new ArrayList<>();
        }

        messageListAdapter.setData(messages);

        // 根据消息列表是否为空来显示或隐藏空视图
        if (messages.isEmpty()) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次恢复活动时刷新消息列表
        loadMessages();
    }
}