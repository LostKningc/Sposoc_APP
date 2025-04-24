package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.google.android.material.button.MaterialButton;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserInfoRepository;
import top.kncweb.sposocapp.remote.models.UserInfo;
import top.kncweb.sposocapp.util.JwtManager;

public class UserInfoActivity extends AppCompatActivity {

    private TextView tvUid, tvHeight, tvWeight, tvSex, tvBmi, tvBmiAssessment;
    private View emptyState, infoCard, metricsCard;
    private MaterialButton btnUpdate;
    private UserInfo userInfo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        // 初始化视图
        initViews();

        // 设置更新按钮点击事件
        setupUpdateButton();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.btn_update), (v, insets) -> {
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 每次返回页面都重新加载数据
        loadUserHealthInfo();
    }

    private void initViews() {
        // 初始化文本视图
        tvUid = findViewById(R.id.tv_uid);
        tvHeight = findViewById(R.id.tv_height);
        tvWeight = findViewById(R.id.tv_weight);
        tvSex = findViewById(R.id.tv_sex);
        tvBmi = findViewById(R.id.tv_bmi);
        tvBmiAssessment = findViewById(R.id.tv_bmi_assessment);

        // 初始化布局视图
        emptyState = findViewById(R.id.empty_state);
        infoCard = findViewById(R.id.info_card);
        metricsCard = findViewById(R.id.metrics_card);

        // 初始化按钮
        btnUpdate = findViewById(R.id.btn_update);
    }

    private void setupUpdateButton() {
        btnUpdate.setOnClickListener(v -> {
            Intent intent = new Intent(UserInfoActivity.this, EditUserInfoActivity.class);
            // 如果有数据，将其传递给编辑页面
            if (userInfo != null) {
                intent.putExtra("userInfo", userInfo);
            }
            startActivity(intent);
        });
    }

    private void loadUserHealthInfo() {
        // 设置用户ID
        long userId = JwtManager.getGlobalUid(this);
        tvUid.setText(String.valueOf(userId));

        // 创建UserInfoRepository实例
        UserInfoRepository userInfoRepository = new UserInfoRepository(JwtManager.getJwtToken(this));

        // 加载用户健康信息
        userInfoRepository.getUserInfo(userId, new SCallback<UserInfo>() {
            @Override
            public void onSuccess(UserInfo info) {
                userInfo = info;
                runOnUiThread(() -> displayHealthInfo(info));
            }

            @Override
            public void onFailure(String msg) {
                Log.e("UserInfoActivity", "获取用户健康信息失败: " + msg);
                runOnUiThread(() -> showEmptyState());
            }
        });
    }

    private void displayHealthInfo(UserInfo info) {
        // 隐藏空状态，显示数据卡片
        emptyState.setVisibility(View.GONE);
        infoCard.setVisibility(View.VISIBLE);
        metricsCard.setVisibility(View.VISIBLE);

        // 设置按钮文本
        btnUpdate.setText("更新健康信息");

        // 显示用户健康数据
        tvHeight.setText(String.valueOf(info.getHeight()));
        // 体重单位是百克，需要转换成千克显示
        tvWeight.setText(String.format("%.1f", info.getWeight() / 10.0));
        // 转换性别显示
        String displaySex = "male".equals(info.getSex()) ? "男" : "female".equals(info.getSex()) ? "女" : "--";
        tvSex.setText(displaySex);

        // 计算并显示BMI
        calculateAndDisplayBMI(info.getHeight(), info.getWeight());
    }

    private void calculateAndDisplayBMI(int heightInCm, int weightInHg) {
        float heightInM = heightInCm / 100f;
        float weightInKg = weightInHg / 10f;

        // 计算BMI
        float bmi = weightInKg / (heightInM * heightInM);
        tvBmi.setText(String.format("%.1f", bmi));

        // BMI评估
        String assessment;
        int textColor;
        if (bmi < 18.5) {
            assessment = "偏瘦";
            textColor = getResources().getColor(android.R.color.holo_blue_light);
        } else if (bmi < 24) {
            assessment = "正常";
            textColor = getResources().getColor(android.R.color.holo_green_dark);
        } else if (bmi < 28) {
            assessment = "偏胖";
            textColor = getResources().getColor(android.R.color.holo_orange_light);
        } else {
            assessment = "肥胖";
            textColor = getResources().getColor(android.R.color.holo_red_light);
        }
        tvBmiAssessment.setText(assessment);
        tvBmiAssessment.setTextColor(textColor);
    }

    private void showEmptyState() {
        // 显示空状态，隐藏数据卡片
        emptyState.setVisibility(View.VISIBLE);
        infoCard.setVisibility(View.GONE);
        metricsCard.setVisibility(View.GONE);

        // 设置按钮文本
        btnUpdate.setText("添加健康信息");
    }
}