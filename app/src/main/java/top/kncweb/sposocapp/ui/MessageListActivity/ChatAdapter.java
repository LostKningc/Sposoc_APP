package top.kncweb.sposocapp.ui.MessageListActivity;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.local.entity.Message;
import top.kncweb.sposocapp.util.JwtManager;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SENT = 0;
    private static final int TYPE_RECEIVED = 1;

    private List<Message> messageList;

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Message> messages) {
        this.messageList = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return messageList.get(position).getSenderUid() == JwtManager.getGlobalUid(SposocApplication.getContext()) ? TYPE_SENT : TYPE_RECEIVED;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_SENT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_sent, parent, false);
            return new SentViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_message_received, parent, false);
            return new ReceivedViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messageList.get(position);
        if (holder instanceof SentViewHolder) {
            ((SentViewHolder) holder).textView.setText(message.getContent());
        } else {
            ((ReceivedViewHolder) holder).textView.setText(message.getContent());
        }
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    static class SentViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        SentViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessageSent);
        }
    }

    static class ReceivedViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ReceivedViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textMessageReceived);
        }
    }
}