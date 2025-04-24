package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.AuthRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.ui.main.MainActivity;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etUid;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private TextView tvRegister;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // 初始化视图
        initViews();
        // 初始化数据仓库
        authRepository = new AuthRepository();
        // 设置事件监听器
        setupListeners();
        // 处理返回键
        setupBackPressed();
        handleRegisteredUid();
    }

    private void initViews() {
        etUid = findViewById(R.id.et_uid);
        etUid.setInputType(InputType.TYPE_CLASS_NUMBER);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvRegister = findViewById(R.id.tv_register);
    }

    private void setupListeners() {
        // 登录按钮点击事件
        btnLogin.setOnClickListener(v -> attemptLogin());

        // 注册文本点击事件
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleRegisteredUid() {
        // 获取从注册页面传来的用户ID
        String registeredUid = getIntent().getStringExtra("registered_uid");
        if (registeredUid != null && !registeredUid.isEmpty()) {
            // 自动填充用户ID
            etUid.setText(registeredUid);
            // 提示用户注册成功
            Toast.makeText(this, "注册成功，请输入密码登录", Toast.LENGTH_SHORT).show();
            // 将焦点放在密码输入框
            etPassword.requestFocus();
        }
    }

    private void attemptLogin() {
        // 获取输入
        String uid = etUid.getText() != null ? etUid.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";

        // 验证输入
        if (uid.isEmpty()) {
            etUid.setError("请输入用户ID");
            etUid.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }

        String encryptedPassword = User.sha256(password);

        // 显示加载状态
        btnLogin.setEnabled(false);
        btnLogin.setText("登录中...");

        // 执行登录
        authRepository.login(uid, encryptedPassword, new SCallback<String>() {
            @Override
            public void onSuccess(String jwt) {
                runOnUiThread(() -> {
                    // 保存JWT令牌
                    JwtManager.setJwtToken(LoginActivity.this, jwt);

                    // 解析JWT获取用户ID并保存
                    try {
                        long userId = Long.parseLong(uid);
                        JwtManager.setGlobalUid(LoginActivity.this, userId);

                        // 跳转到主页
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } catch (Exception e) {
                        Log.e("JWT解析", "无法解析用户ID", e);
                        Toast.makeText(LoginActivity.this, "登录成功但无法解析用户信息", Toast.LENGTH_SHORT).show();
                        resetLoginButton();
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                runOnUiThread(() -> {
                    Toast.makeText(LoginActivity.this, "登录失败: " + msg, Toast.LENGTH_SHORT).show();
                    resetLoginButton();
                });
            }
        });
    }

    private void resetLoginButton() {
        btnLogin.setEnabled(true);
        btnLogin.setText("登 录");
    }

    private void setupBackPressed() {
        getOnBackPressedDispatcher().addCallback(this, new androidx.activity.OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                moveTaskToBack(true);
            }
        });
    }
}