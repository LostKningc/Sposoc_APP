package top.kncweb.sposocapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.util.JwtManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button reg_button = findViewById(R.id.bt_toReg);
        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        Button login_button = findViewById(R.id.bt_toLogin);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Button jwt_button = findViewById(R.id.bt_jwtTest);
        TextView jwtStatus_tv = findViewById(R.id.tv_jwtStatus);
        jwt_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(JwtManager.getJwtToken(MainActivity.this)!= null && !JwtManager.getJwtToken(MainActivity.this).isEmpty()){
                    jwtStatus_tv.setText("JWT Exists");
                }else{
                    jwtStatus_tv.setText("JWT NOT Exists");
                }
            }
        });

        Button userInfo_button = findViewById(R.id.bt_toInfo);
        userInfo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivity(intent);
            }
        });

    }


}