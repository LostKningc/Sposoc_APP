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

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button reg_button = findViewById(R.id.bt_reg);
        TextView userName = findViewById(R.id.tv_regName);
        TextView password = findViewById(R.id.tv_regPassword);

        UserRepository userRepository = new UserRepository();

        reg_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                User user = new User(userName.getText().toString(),password.getText().toString(),"0");
                userRepository.addUser(user, new SCallback<Integer>() {
                    @Override
                    public void onSuccess(Integer uid) {
                        Log.d("Uid",uid.toString());
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