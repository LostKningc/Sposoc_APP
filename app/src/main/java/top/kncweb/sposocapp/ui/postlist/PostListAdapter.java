package top.kncweb.sposocapp.ui.postlist;

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
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.models.Post;

public class PostListAdapter extends RecyclerView.Adapter<PostListAdapter.ViewHolder>{

    private List<Post> postList = new ArrayList<>();

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Post> list) {
        this.postList = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postList.get(position);
        String username = UserCacheManager.get(post.getUid(), SposocApplication.getContext(), new SCallback<String>() {
            @Override
            public void onSuccess(String username) {
                holder.username.setText(username);
            }
            @Override
            public void onFailure(String msg) {
                holder.username.setText("Unknown");
            }
        });
        holder.username.setText(Objects.requireNonNullElse(username, "Loading..."));
        holder.time.setText(post.getTime());
        holder.content.setText(post.getContent());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PostDetailActivity.class);
                intent.putExtra("post", post);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        TextView time;
        TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.cd_tv_username);
            time = itemView.findViewById(R.id.cd_tv_time);
            content = itemView.findViewById(R.id.cd_tv_content);
        }
    }
}
