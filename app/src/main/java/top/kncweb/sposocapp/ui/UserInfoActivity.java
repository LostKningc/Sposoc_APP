package top.kncweb.sposocapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserInfoRepository;
import top.kncweb.sposocapp.remote.models.UserInfo;
import top.kncweb.sposocapp.util.JwtManager;

public class UserInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tn_uid = findViewById(R.id.tn_uid);
        TextView tn_height = findViewById(R.id.tn_height);
        TextView tn_weight = findViewById(R.id.tn_weight);
        RadioGroup rg_sex = findViewById(R.id.rg_sex);

        Button bt_get = findViewById(R.id.bt_getInfo);
        Button bt_add = findViewById(R.id.bt_addInfo);
        Button bt_update = findViewById(R.id.bt_updateInfo);

        bt_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoRepository userInfoRepository = new UserInfoRepository(JwtManager.getJwtToken(UserInfoActivity.this));
                userInfoRepository.getUserInfo(Integer.parseInt(tn_uid.getText().toString()), new SCallback<UserInfo>() {
                    @Override
                    public void onSuccess(UserInfo info) {
                        tn_height.setText(String.valueOf(info.getHeight()));
                        tn_weight.setText(String.valueOf(info.getWeight()));
                        ((RadioButton)rg_sex.getChildAt(info.getSex().equals("male") ? 0 : 1)).setChecked(true);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("UserInfo",msg);
                    }
                });
            }
        });

        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoRepository userInfoRepository = new UserInfoRepository(JwtManager.getJwtToken(UserInfoActivity.this));

                UserInfo userInfo = new UserInfo(Integer.parseInt(tn_uid.getText().toString()),
                        Integer.parseInt(tn_height.getText().toString()),
                        Integer.parseInt(tn_weight.getText().toString()),
                        getSex(rg_sex));

                userInfoRepository.addUserInfo(userInfo, new SCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer uid) {
                        Log.d("addInfo",String.valueOf(uid));
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("addInfo",msg);
                    }
                });
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserInfoRepository userInfoRepository = new UserInfoRepository(JwtManager.getJwtToken(UserInfoActivity.this));
                int uid = Integer.parseInt(tn_uid.getText().toString());
                Integer height = tn_height.getText().toString().trim().isEmpty()? null : Integer.parseInt(tn_height.getText().toString());
                Integer weight = tn_weight.getText().toString().trim().isEmpty()? null : Integer.parseInt(tn_weight.getText().toString());

                userInfoRepository.updateUserInfo(uid, height, weight, getSex(rg_sex), new SCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean success) {
                        Log.d("infoUpdate", "Success");
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("infoUpdate", msg);
                    }
                });
            }
        });
    }

    private String getSex(@NonNull RadioGroup rg_sex){
        int selectedId = rg_sex.getCheckedRadioButtonId();
        if(selectedId == -1){
            return null;
        }else {
            RadioButton selectedRadioButton = findViewById(selectedId);
            return selectedRadioButton.getText().toString();
        }
    }
}