package top.kncweb.sposocapp.ui.friendlist;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.remote.dao.FriendshipRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.models.Friendship;
import top.kncweb.sposocapp.remote.models.User;
import top.kncweb.sposocapp.util.JwtManager;

public class FriendRequestListAdapter extends RecyclerView.Adapter<FriendRequestListAdapter.ViewHolder>{

    private List<User> friendRequestList = new ArrayList<>();
    private final FriendshipRepository friendshipRepository = new FriendshipRepository(JwtManager.getJwtToken(SposocApplication.getContext()));

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<User> list) {
        this.friendRequestList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_request_card, parent, false);
        return new FriendRequestListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = friendRequestList.get(position);
        holder.userName.setText(user.getName());
        holder.uid.setText(String.valueOf(user.getUid()));
        holder.acceptButton.setOnClickListener(v -> {
            // Handle accept friend request
            friendshipRepository.acceptFriend(user.getUid(), JwtManager.getGlobalUid(SposocApplication.getContext()), new SCallback<Long>() {
                @Override
                public void onSuccess(Long uid) {
                    friendRequestList.remove(position);
                    notifyItemRemoved(position);
                }

                @Override
                public void onFailure(String msg) {
                    Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });
        });
        holder.rejectButton.setOnClickListener(v -> {
            friendshipRepository.rejectFriend(user.getUid(), JwtManager.getGlobalUid(SposocApplication.getContext()), new SCallback<Long>() {
                @Override
                public void onSuccess(Long uid) {
                    friendRequestList.remove(position);
                    notifyItemRemoved(position);
                }

                @Override
                public void onFailure(String msg) {
                    Log.e("FriendRequestListAdapter", msg);
                    Toast.makeText(v.getContext(), msg, Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

    @Override
    public int getItemCount() {
        return friendRequestList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView userName;
        TextView uid;
        ImageButton acceptButton;
        ImageButton rejectButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.fq_username);
            uid = itemView.findViewById(R.id.fq_uid);
            acceptButton = itemView.findViewById(R.id.fq_button_accept);
            rejectButton = itemView.findViewById(R.id.fq_button_reject);
        }
    }
}
