package top.kncweb.sposocapp.ui.friendlist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import top.kncweb.sposocapp.remote.models.Friendship;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class AddFriendActivity extends AppCompatActivity {

    FriendshipRepository friendshipRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_friend);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.layout_add_friend), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button addFriendButton = findViewById(R.id.af_button_send_request);
        TextView addFriendText = findViewById(R.id.af_edit_uid_input);
        friendshipRepository = new FriendshipRepository(JwtManager.getJwtToken(this));

        RecyclerView friendRequests = findViewById(R.id.recycler_friend_requests);
        friendRequests.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));
        FriendRequestListAdapter friendRequestListAdapter = new FriendRequestListAdapter();
        friendRequests.setAdapter(friendRequestListAdapter);

        friendshipRepository.getFriendRequests(JwtManager.getGlobalUid(SposocApplication.getContext()), new SCallback<List<User>>() {
            @Override
            public void onSuccess(List<User> userlist) {
                friendRequestListAdapter.setData(userlist);
            }

            @Override
            public void onFailure(String msg) {
                friendRequestListAdapter.setData(null);
            }
        });

        addFriendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(addFriendText.getText().toString().isEmpty()){
                    addFriendText.setError("Please enter a valid UID");
                    return;
                }
                long uid = Long.parseLong(addFriendText.getText().toString());
                friendshipRepository.addFriend(JwtManager.getGlobalUid(SposocApplication.getContext()), uid, new SCallback<Friendship>() {
                    @Override
                    public void onSuccess(Friendship friendship) {
                        addFriendText.setText("");
                        addFriendText.setError(null);
                    }

                    @Override
                    public void onFailure(String msg) {
                        addFriendText.setError(msg);
                    }
                });
            }
        });
    }
}