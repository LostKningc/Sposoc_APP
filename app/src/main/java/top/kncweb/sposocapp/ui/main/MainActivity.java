package top.kncweb.sposocapp.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.dao.AuthRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.ui.LoginActivity;
import top.kncweb.sposocapp.util.JwtManager;

public class MainActivity extends AppCompatActivity {

    Fragment sportFragment = new SportFragment();
    Fragment communityFragment = new CommunityFragment();
    Fragment profileFragment = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!JwtManager.isLoggedIn(MainActivity.this)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

//        AuthRepository authRepository = new AuthRepository();
//        authRepository.validate(JwtManager.getJwtToken(this), new SCallback<Boolean>() {
//            @Override
//            public void onSuccess(Boolean result) {
//                if (!result) {
//                    JwtManager.clearJwtToken(MainActivity.this);
//                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }
//            @Override
//            public void onFailure(String msg) {
//                JwtManager.clearJwtToken(MainActivity.this);
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                startActivity(intent);
//                finish();
//            }
//        });


        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadFragment(sportFragment);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_sport) {
                loadFragment(sportFragment);
                return true;
            } else if (id == R.id.nav_community) {
                loadFragment(communityFragment);
                return true;
            } else if (id == R.id.nav_profile) {
                loadFragment(profileFragment);
                return true;
            }
            return false;
        });

    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .commit();
    }


}