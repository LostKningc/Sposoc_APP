package top.kncweb.sposocapp.ui.MessageListActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Objects;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.enums.MessageType;
import top.kncweb.sposocapp.local.entity.Message;
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.util.JwtManager;
import top.kncweb.sposocapp.viewmodel.ChatViewModel;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private EditText messageInput;
    private FloatingActionButton sendButton;
    private TextView chatTitle;
    private LinearLayout emptyStateView;
    private long receiverUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取传递的用户ID
        receiverUid = getIntent().getLongExtra("uid", -1);
        if (receiverUid == -1) {
            throw new IllegalArgumentException("User ID is required");
        }

        // 初始化视图
        initViews();

        // 初始化ViewModel并观察数据
        initViewModel();

        // 加载聊天对象信息
        loadUserInfo();
    }

    private void initViews() {
        // 初始化控件
        recyclerView = findViewById(R.id.rv_messages);
        messageInput = findViewById(R.id.et_message);
        sendButton = findViewById(R.id.btn_send);
        chatTitle = findViewById(R.id.tv_chat_title);
        emptyStateView = findViewById(R.id.empty_state);
        ImageButton backButton = findViewById(R.id.btn_back);

        // 设置RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setStackFromEnd(true); // 从底部开始布局
        recyclerView.setLayoutManager(layoutManager);

        chatAdapter = new ChatAdapter();
        recyclerView.setAdapter(chatAdapter);

        // 设置返回按钮点击事件
        backButton.setOnClickListener(v -> finish());

        // 设置发送按钮点击事件
        sendButton.setOnClickListener(v -> sendMessage());
    }

    private void initViewModel() {
        ChatViewModel viewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(ChatViewModel.class);

        viewModel.setUid(receiverUid);

        // 观察消息列表变化
        viewModel.getMessages().observe(this, messages -> {
            if (messages != null && !messages.isEmpty()) {
                chatAdapter.setData(messages);
                recyclerView.scrollToPosition(messages.size() - 1);
                emptyStateView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            } else {
                emptyStateView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
    }

    private void loadUserInfo() {
        String username;
        username = UserCacheManager.get(receiverUid, this, new SCallback<String>() {
            @Override
            public void onSuccess(String name) {
                chatTitle.setText(name);
            }
            @Override
            public void onFailure(String msg) {
                chatTitle.setText("获取用户名失败");
            }
        });
        chatTitle.setText(Objects.requireNonNullElse(username, "loading..."));
    }

    private void sendMessage() {
        String content = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(content)) {
            return;
        }

        ChatViewModel viewModel = new ViewModelProvider(this).get(ChatViewModel.class);

        Message message = new Message();
        message.setSenderUid(JwtManager.getGlobalUid(SposocApplication.getContext()));
        message.setReceiverUid(receiverUid);
        message.setContent(content);
        message.setType(MessageType.normal);

        viewModel.sendMessage(message);
        messageInput.setText("");
    }
}