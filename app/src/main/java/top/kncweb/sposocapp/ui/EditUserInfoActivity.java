package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserInfoRepository;
import top.kncweb.sposocapp.remote.models.UserInfo;
import top.kncweb.sposocapp.util.JwtManager;

public class EditUserInfoActivity extends AppCompatActivity {

    private TextView tvUid;
    private TextInputEditText etHeight, etWeight;
    private RadioGroup rgSex;
    private RadioButton rbMale, rbFemale;
    private MaterialButton btnSave, btnCancel;
    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_info);

        // 初始化视图
        initViews();

        // 从Intent获取数据
        getIntentData();

        // 设置按钮监听器
        setupListeners();
    }

    private void initViews() {
        tvUid = findViewById(R.id.tv_uid);
        etHeight = findViewById(R.id.et_height);
        etWeight = findViewById(R.id.et_weight);
        rgSex = findViewById(R.id.rg_sex);
        rbMale = findViewById(R.id.rb_male);
        rbFemale = findViewById(R.id.rb_female);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
    }

    private void getIntentData() {
        // 设置用户ID（只读）
        long userId = JwtManager.getGlobalUid(this);
        tvUid.setText(String.valueOf(userId));

        // 获取传递的用户信息数据
        Intent intent = getIntent();
        UserInfo userInfo = (UserInfo) intent.getSerializableExtra("userInfo");

        // 检查是否有传入数据，判断是新增还是更新
        isUpdate = userInfo != null;

        // 如果有数据，填充到表单
        if (isUpdate) {
            etHeight.setText(String.valueOf(userInfo.getHeight()));
            //体重单位是百克,需要转换成千克
            etWeight.setText(String.valueOf(userInfo.getWeight() / 10.0));
            if (userInfo.getSex().equals("male")) {
                rbMale.setChecked(true);
            } else if (userInfo.getSex().equals("female")) {
                rbFemale.setChecked(true);
            }
        }
    }

    private void setupListeners() {
        // 取消按钮
        btnCancel.setOnClickListener(v -> finish());

        // 保存按钮
        btnSave.setOnClickListener(v -> {
            if (validateInputs()) {
                saveUserHealthInfo();
            }
        });
    }

    private boolean validateInputs() {
        boolean isValid = true;

        // 验证身高
        String heightStr = etHeight.getText().toString().trim();
        if (heightStr.isEmpty()) {
            etHeight.setError("请输入身高");
            isValid = false;
        } else {
            try {
                int height = Integer.parseInt(heightStr);
                if (height <= 0 || height > 300) {
                    etHeight.setError("身高数值不合理");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etHeight.setError("请输入有效数字");
                isValid = false;
            }
        }

        // 验证体重
        String weightStr = etWeight.getText().toString().trim();
        if (weightStr.isEmpty()) {
            etWeight.setError("请输入体重");
            isValid = false;
        } else {
            try {
                float weight = Float.parseFloat(weightStr);
                if (weight <= 0 || weight > 500) {
                    etWeight.setError("体重数值不合理");
                    isValid = false;
                }
            } catch (NumberFormatException e) {
                etWeight.setError("请输入有效数字");
                isValid = false;
            }
        }

        // 验证性别选择
        if (rgSex.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "请选择性别", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void saveUserHealthInfo() {
        // 获取用户输入
        int userId = Integer.parseInt(tvUid.getText().toString());
        int height = Integer.parseInt(etHeight.getText().toString().trim());
        float weight = Float.parseFloat(etWeight.getText().toString().trim());
        int weightInHundredGrams = (int) (weight * 10); // 转换为百克
        String sexValue = getSex(rgSex);

        // 创建UserInfoRepository实例
        UserInfoRepository userInfoRepository = new UserInfoRepository(JwtManager.getJwtToken(this));

        // 根据isUpdate标志决定调用add还是update方法
        if (isUpdate) {
            // 更新用户信息
            userInfoRepository.updateUserInfo(userId, height, weightInHundredGrams, sexValue, new SCallback<Boolean>() {
                @Override
                public void onSuccess(Boolean success) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditUserInfoActivity.this, "健康信息更新成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onFailure(String msg) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditUserInfoActivity.this, "更新失败：" + msg, Toast.LENGTH_SHORT).show();
                        Log.e("EditUserInfo", "更新失败: " + msg);
                    });
                }
            });
        } else {
            // 添加新用户信息
            UserInfo userInfo = new UserInfo(userId, height, weightInHundredGrams, sexValue);
            userInfoRepository.addUserInfo(userInfo, new SCallback<Integer>() {
                @Override
                public void onSuccess(Integer uid) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditUserInfoActivity.this, "健康信息添加成功", Toast.LENGTH_SHORT).show();
                        finish();
                    });
                }

                @Override
                public void onFailure(String msg) {
                    runOnUiThread(() -> {
                        Toast.makeText(EditUserInfoActivity.this, "添加失败：" + msg, Toast.LENGTH_SHORT).show();
                        Log.e("EditUserInfo", "添加失败: " + msg);
                    });
                }
            });
        }
    }

    private String getSex(@NonNull RadioGroup rg_sex) {
        int selectedId = rg_sex.getCheckedRadioButtonId();
        if (selectedId == -1) {
            return null;
        } else {
            RadioButton selectedRadioButton = findViewById(selectedId);
            if(selectedRadioButton.equals(rbMale)){
                return "male";
            }else{
                return "female";
            }
        }
    }
}