package top.kncweb.sposocapp.ui.MessageListActivity;

import static androidx.core.content.ContextCompat.startActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.local.entity.Message;
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.util.FormatFriendlyTime;
import top.kncweb.sposocapp.util.JwtManager;

public class MessageListAdapter extends RecyclerView.Adapter<MessageListAdapter.ViewHolder> {

    private List<Message> messageList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Message> list) {
        this.messageList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.message_item, parent, false);
        return new MessageListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messageList.get(position);
        Long thatUid = message.getSenderUid()== JwtManager.getGlobalUid(SposocApplication.getContext()) ? message.getReceiverUid() : message.getSenderUid();
        String username = UserCacheManager.get(thatUid, SposocApplication.getContext(), new SCallback<String>() {
            @Override
            public void onSuccess(String username) {
                holder.usernameTextView.setText(username);
            }
            @Override
            public void onFailure(String msg) {
                holder.textView.setText("Unknown");
            }
        });
        holder.usernameTextView.setText(Objects.requireNonNullElse(username, "Loading..."));
        holder.textView.setText(message.getContent());
        holder.timeTextView.setText(FormatFriendlyTime.formatFriendlyTime(message.getSent_time()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ChatActivity.class);
                intent.putExtra("uid", thatUid);
                startActivity(v.getContext(), intent, null);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public void addData(Message message) {
        this.messageList.add(message);
        notifyItemInserted(messageList.size() - 1);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView usernameTextView;
        TextView textView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.mcd_tv_username);
            textView = itemView.findViewById(R.id.mcd_tv_message);
            timeTextView = itemView.findViewById(R.id.mcd_tv_time);
        }
    }
}
