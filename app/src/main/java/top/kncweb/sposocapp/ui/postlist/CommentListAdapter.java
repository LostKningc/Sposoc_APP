package top.kncweb.sposocapp.ui.postlist;

import android.annotation.SuppressLint;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.models.Comment;
import top.kncweb.sposocapp.remote.dao.SCallback;

import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;


public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {

    private List<Comment> commentList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Comment> list) {
        this.commentList = list;
        notifyDataSetChanged();
    }

    public void addComment(Comment comment) {
        commentList.add(0, comment); // 添加到顶部
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentListAdapter.ViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        String username = UserCacheManager.get(comment.getUid(), holder.usernameTextView.getContext(), new SCallback<String>() {
            @Override
            public void onSuccess(String name) {
                holder.usernameTextView.setText(name);
            }

            @Override
            public void onFailure(String msg) {
                holder.usernameTextView.setText("Unknown User");
            }
        });
        if (username != null) {
            holder.usernameTextView.setText(username);
        } else {
            holder.usernameTextView.setText("Loading...");
        }
        holder.contentTextView.setText(comment.getContent());
        holder.timeTextView.setText(comment.getTime());


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatarImageView;
        TextView usernameTextView;
        TextView contentTextView;
        TextView timeTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.iv_comment_avatar);
            usernameTextView = itemView.findViewById(R.id.tv_comment_username);
            contentTextView = itemView.findViewById(R.id.tv_comment_content);
            timeTextView = itemView.findViewById(R.id.tv_comment_time);
        }
    }
}

