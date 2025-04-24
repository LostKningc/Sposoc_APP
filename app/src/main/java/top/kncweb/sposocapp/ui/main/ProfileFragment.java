package top.kncweb.sposocapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.ui.ActivityRecordActivity;
import top.kncweb.sposocapp.ui.LoginActivity;
import top.kncweb.sposocapp.ui.MessageListActivity.MessageListActivity;
import top.kncweb.sposocapp.ui.UserInfoActivity;
import top.kncweb.sposocapp.ui.friendlist.FriendListActivity;
import top.kncweb.sposocapp.util.JwtManager;

public class ProfileFragment extends Fragment {

    private TextView tvUsername;
    private TextView tvUid;
    private View llMessage;
    private View llFriends;
    private View llHealthInfo;
    private View btnLogout;

    public ProfileFragment() {
        // 必需的空构造函数
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // 初始化视图
        initViews(view);

        // 设置用户信息
        setUserInfo();

        // 设置点击事件
        setupClickListeners();

        return view;
    }

    private void initViews(View view) {
        tvUsername = view.findViewById(R.id.tv_username);
        tvUid = view.findViewById(R.id.tv_uid);
        llMessage = view.findViewById(R.id.ll_message);
        llFriends = view.findViewById(R.id.ll_friends);
        llHealthInfo = view.findViewById(R.id.ll_health_info);
        btnLogout = view.findViewById(R.id.btn_logout);
    }

    private void setUserInfo() {
        // 从 SharedPreferences 获取用户 ID
        long userId = JwtManager.getGlobalUid(requireContext());

        // 设置 UID
        if (userId != -1) {
            tvUid.setText(String.valueOf(userId));
        } else {
            tvUid.setText("未登录");
        }

        String username;
        username = UserCacheManager.get(userId, requireContext(), new SCallback<String>() {
            @Override
            public void onSuccess(String name) {
                tvUsername.setText(name);
            }

            @Override
            public void onFailure(String msg) {
                tvUsername.setText("获取用户名失败");
            }
        });
        tvUsername.setText(Objects.requireNonNullElse(username, "loading..."));
    }

    private void setupClickListeners() {
        // 消息点击事件
        llMessage.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), MessageListActivity.class);
            startActivity(intent);
        });

        // 好友点击事件
        llFriends.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), FriendListActivity.class);
            startActivity(intent);
        });

        // 健康信息点击事件
        llHealthInfo.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), UserInfoActivity.class);
            startActivity(intent);
        });

        // 登出点击事件
        btnLogout.setOnClickListener(v -> {
            // 显示确认对话框
            androidx.appcompat.app.AlertDialog.Builder builder =
                    new androidx.appcompat.app.AlertDialog.Builder(requireContext());
            builder.setTitle("确认登出")
                    .setMessage("确定要退出登录吗？")
                    .setPositiveButton("确定", (dialog, which) -> {
                        // 清除 JWT 令牌和用户 ID
                        JwtManager.clearJwtToken(requireContext());
                        JwtManager.setGlobalUid(requireContext(), -1);

                        // 跳转到登录页面
                        Intent intent = new Intent(requireContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);

                        Toast.makeText(requireContext(), "已登出", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("取消", null)
                    .show();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // 页面恢复时刷新用户信息
        setUserInfo();
    }
}