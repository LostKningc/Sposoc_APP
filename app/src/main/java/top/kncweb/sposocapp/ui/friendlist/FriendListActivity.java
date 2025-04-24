package top.kncweb.sposocapp.ui.friendlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.remote.dao.FriendshipRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class FriendListActivity extends AppCompatActivity {

    private FriendListAdapter adapter;
    private RecyclerView recyclerView;
    private FriendshipRepository dao;
    private TextView emptyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_friend_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_friend_list), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recycler_friend_list);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        LinearLayout addFriendLayout = findViewById(R.id.layout_new_friends);

        addFriendLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddFriendActivity.class);
                startActivity(intent);
            }
        });

        adapter = new FriendListAdapter();
        recyclerView.setAdapter(adapter);
        emptyText = findViewById(R.id.fr_text_empty);
        dao = new FriendshipRepository(JwtManager.getJwtToken(FriendListActivity.this));
        dao.getFriends(JwtManager.getGlobalUid(SposocApplication.getContext()), new SCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> users) {
                if(users == null || users.isEmpty()) {
                    emptyText.setVisibility(View.VISIBLE);
                    return;
                }
                runOnUiThread(() -> adapter.setData(users));
            }
            @Override
            public void onFailure(String msg) {
                runOnUiThread(() -> {
                    Log.e("FriendList", msg);
                    Toast.makeText(FriendListActivity.this, msg, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }
}