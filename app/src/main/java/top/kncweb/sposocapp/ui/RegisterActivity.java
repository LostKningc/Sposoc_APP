package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.AuthRepository;
import top.kncweb.sposocapp.remote.models.User;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnRegister;
    private View ivBack;
    private TextView tvToLogin;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        // 初始化视图
        initViews();
        // 初始化数据仓库
        authRepository = new AuthRepository();
        // 设置事件监听器
        setupListeners();
    }

    private void initViews() {
        etUsername = findViewById(R.id.tv_regName);
        etPassword = findViewById(R.id.tv_regPassword);
        etConfirmPassword = findViewById(R.id.tv_regConfirmPassword);
        btnRegister = findViewById(R.id.bt_reg);
        ivBack = findViewById(R.id.iv_back);
        tvToLogin = findViewById(R.id.tv_to_login);
    }

    private void setupListeners() {
        // 注册按钮点击事件
        btnRegister.setOnClickListener(v -> attemptRegister());

        // 返回按钮点击事件
        ivBack.setOnClickListener(v -> finish());

        // 登录文本点击事件
        tvToLogin.setOnClickListener(v -> {
            finish(); // 关闭当前注册页面，返回到登录页面
        });
    }

    private void attemptRegister() {
        // 获取输入
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String confirmPassword = etConfirmPassword.getText() != null ? etConfirmPassword.getText().toString().trim() : "";

        // 验证输入
        if (username.isEmpty()) {
            etUsername.setError("请输入用户名");
            etUsername.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("请输入密码");
            etPassword.requestFocus();
            return;
        }

        if (confirmPassword.isEmpty()) {
            etConfirmPassword.setError("请确认密码");
            etConfirmPassword.requestFocus();
            return;
        }

        if (!password.equals(confirmPassword)) {
            etConfirmPassword.setError("两次输入的密码不一致");
            etConfirmPassword.requestFocus();
            return;
        }

        // 显示加载状态
        btnRegister.setEnabled(false);
        btnRegister.setText("注册中...");

        // 执行注册
        User user = new User(username, password, "0");
        authRepository.addUser(user, new SCallback<Integer>() {
            @Override
            public void onSuccess(Integer uid) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "注册成功，您的用户ID是：" + uid, Toast.LENGTH_LONG).show();

                    // 可以选择自动跳转到登录页面
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.putExtra("registered_uid", uid.toString());
                    startActivity(intent);
                    finish();
                });
            }

            @Override
            public void onFailure(String msg) {
                runOnUiThread(() -> {
                    Toast.makeText(RegisterActivity.this, "注册失败: " + msg, Toast.LENGTH_SHORT).show();
                    resetRegisterButton();
                    Log.e("注册错误", msg);
                });
            }
        });
    }

    private void resetRegisterButton() {
        btnRegister.setEnabled(true);
        btnRegister.setText(getString(R.string.reg_button_name));
    }
}