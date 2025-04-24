package top.kncweb.sposocapp.ui.postlist;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.databinding.ActivityPostDetailBinding;
import top.kncweb.sposocapp.databinding.SportRecordCardBinding;
import top.kncweb.sposocapp.local.entity.ActivityRecord;
import top.kncweb.sposocapp.remote.Cache.UserCacheManager;
import top.kncweb.sposocapp.remote.dao.ActivityRecordRepository;
import top.kncweb.sposocapp.remote.dao.CommentRepository;
import top.kncweb.sposocapp.remote.dao.LikeRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.models.Comment;
import top.kncweb.sposocapp.remote.models.Post;
import top.kncweb.sposocapp.ui.SportRecordCardBinder;
import top.kncweb.sposocapp.util.JwtManager;

public class PostDetailActivity extends AppCompatActivity {

    CommentListAdapter commentListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_detail);
        ActivityPostDetailBinding binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        ViewCompat.setOnApplyWindowInsetsListener(binding.clPost, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left + 30, systemBars.top + 30, systemBars.right + 30, systemBars.bottom + 30);
            return insets;
        });

        setContentView(binding.getRoot());

        Post post = (Post) getIntent().getSerializableExtra("post");
        if (post == null) {
            Log.e("PostDetail", "Post is null");
            this.finish();
        }


        commentListAdapter = new CommentListAdapter();
        binding.rvComments.setAdapter(commentListAdapter);
        binding.rvComments.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(this));

        String username = UserCacheManager.get(post.getUid(), this, new SCallback<String>() {
            @Override
            public void onSuccess(String username) {
                binding.tvUsername.setText(username);
            }

            @Override
            public void onFailure(String msg) {
                binding.tvUsername.setText("User: Unknown");
            }
        });
        if (username != null) {
            binding.tvUsername.setText(username);
        }
        binding.tvContent.setText(post.getContent());
        binding.tvTime.setText(post.getTime());

        if (post.getRid() != 0) {
            ActivityRecordRepository dao = new ActivityRecordRepository(JwtManager.getJwtToken(PostDetailActivity.this));
            SportRecordCardBinding cardBinding = binding.sportRecordCard;
            dao.getActivityRecord(post.getRid(), new SCallback<ActivityRecord>() {
                @Override
                public void onSuccess(ActivityRecord activityRecord) {
                    runOnUiThread(() -> {
                        new SportRecordCardBinder(cardBinding).bind(activityRecord);
                    });
                }

                @Override
                public void onFailure(String msg) {
                    Log.e("PostDetail", msg);
                }
            });
        }

        AtomicBoolean isLiked = new AtomicBoolean(false);
        AtomicLong likeNum = new AtomicLong(0);
        AtomicLong commentNum = new AtomicLong(0);

        LikeRepository likeRepository = new LikeRepository(JwtManager.getJwtToken(this));
        CommentRepository commentRepository = new CommentRepository(JwtManager.getJwtToken(this));
        likeRepository.isLiked(post.getPid(), new SCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean liked) {
                runOnUiThread(() -> {
                    if (liked) {
                        binding.ivLike.setImageResource(R.drawable.ic_like_filled);
                        isLiked.set(true);
                    } else {
                        binding.ivLike.setImageResource(R.drawable.ic_like_outline);
                        isLiked.set(false);
                    }
                });
            }

            @Override
            public void onFailure(String msg) {
                Log.e("PostDetail", msg);
            }
        });

        binding.llLike.setOnClickListener(v -> {
            if (isLiked.get()) {
                likeRepository.deleteLike(post.getUid(), post.getPid(), new SCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        runOnUiThread(() -> {
                            binding.ivLike.setImageResource(R.drawable.ic_like_outline);
                            isLiked.set(false);
                            likeNum.decrementAndGet();
                            binding.tvLikeCount.setText(String.valueOf(likeNum.get()));
                        });
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("PostDetail", msg);
                    }
                });
            } else {
                likeRepository.addLike(post.getUid(), post.getPid(), new SCallback<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {
                        runOnUiThread(() -> {
                            binding.ivLike.setImageResource(R.drawable.ic_like_filled);
                            isLiked.set(true);
                            likeNum.incrementAndGet();
                            binding.tvLikeCount.setText(String.valueOf(likeNum.get()));
                        });
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("PostDetail", msg);
                    }
                });
            }
        });

        likeRepository.getLikeNum(post.getPid(), new SCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                runOnUiThread(() -> {
                    likeNum.set(aLong);
                    binding.tvLikeCount.setText(String.valueOf(aLong));
                });
            }

            @Override
            public void onFailure(String msg) {
                Log.e("PostDetail", msg);
            }
        });

        commentRepository.getCommentNum(post.getPid(), new SCallback<Long>() {
            @Override
            public void onSuccess(Long aLong) {
                runOnUiThread(() -> {
                    commentNum.set(aLong);
                    binding.tvCommentCount.setText(String.valueOf(aLong));
                });
            }

            @Override
            public void onFailure(String msg) {
                Log.e("PostDetail", msg);
            }
        });

        commentRepository.getComments(post.getPid(), new SCallback<List<Comment>>() {
            @Override
            public void onSuccess(List<Comment> commentList) {
                runOnUiThread(() -> {
                    commentListAdapter.setData(commentList);
                });
            }
            @Override
            public void onFailure(String msg) {
                Log.e("PostDetail", msg);
            }
        });

        LinearLayout commentInputContainer = binding.commentInputContainer;
        EditText etComment = binding.etComment;
        ImageButton btnSend = binding.btnSend;

        binding.llComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 显示评论输入区域
                commentInputContainer.setVisibility(View.VISIBLE);
                etComment.requestFocus();

                // 可选：自动弹出软键盘
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(etComment, InputMethodManager.SHOW_IMPLICIT);

            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = etComment.getText().toString();
                if (commentContent.isEmpty()) {
                    Toast.makeText(PostDetailActivity.this, "评论内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Comment comment = new Comment();
                comment.setUid(JwtManager.getGlobalUid(SposocApplication.getContext()));
                comment.setPid(post.getPid());
                comment.setContent(commentContent);
                // 发送评论
                commentRepository.addComment(comment, new SCallback<Long>() {
                    @Override
                    public void onSuccess(Long aLong) {
                        runOnUiThread(() -> {
                            comment.setTime("刚刚");
                            commentListAdapter.addComment(comment);
                            etComment.setText("");
                            commentInputContainer.setVisibility(View.GONE);
                            commentNum.incrementAndGet();
                            binding.tvCommentCount.setText(String.valueOf(commentNum.get()));
                            Toast.makeText(PostDetailActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onFailure(String msg) {
                        Log.e("PostDetail", msg);
                        Toast.makeText(PostDetailActivity.this, "评论失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}