package top.kncweb.sposocapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.dao.UserRepository;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button login_button = findViewById(R.id.bt_login);
        TextView uid = findViewById(R.id.tv_loginName);
        TextView password = findViewById(R.id.tv_loginPassword);

        UserRepository userRepository = new UserRepository();

        login_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                User user = new User("",password.getText().toString(),uid.getText().toString());
                userRepository.login(user.getUid(), user.getPassword(), new SCallback<String>() {
                    @Override
                    public void onSuccess(String jwt) {
                        JwtManager.setJwtToken(LoginActivity.this,jwt);
                        Log.d("Uid",jwt);
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("Error",msg);
                    }
                });
            }
        });
    }
}