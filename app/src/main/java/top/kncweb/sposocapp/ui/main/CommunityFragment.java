package top.kncweb.sposocapp.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.materialswitch.MaterialSwitch;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import top.kncweb.sposocapp.R;
import top.kncweb.sposocapp.SposocApplication;
import top.kncweb.sposocapp.remote.dao.PostRepository;
import top.kncweb.sposocapp.remote.dao.SCallback;
import top.kncweb.sposocapp.remote.models.Post;
import top.kncweb.sposocapp.ui.postlist.PostListAdapter;
import top.kncweb.sposocapp.util.JwtManager;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommunityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommunityFragment extends Fragment {

    public CommunityFragment() {
        // Required empty public constructor
    }

    private PostListAdapter adapter;
    private androidx.recyclerview.widget.RecyclerView recyclerView;
    private PostRepository dao;
    private EditText editPost;
    private ImageButton btnSend;
    private MaterialSwitch switchAnonymous;
    private List<Post> postList;


    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        recyclerView = view.findViewById(R.id.post_list);
        recyclerView.setLayoutManager(new androidx.recyclerview.widget.LinearLayoutManager(view.getContext()));

        adapter = new PostListAdapter();
        recyclerView.setAdapter(adapter);

        dao = new PostRepository(JwtManager.getJwtToken(view.getContext()));
        dao.getPosts(JwtManager.getGlobalUid(SposocApplication.getContext()), new SCallback<List<Post>>() {
            @Override
            public void onSuccess(List<Post> posts) {
                if(isAdded()) {
                    requireActivity().runOnUiThread(() -> adapter.setData(posts));
                    postList = posts;
                }
            }
            @Override
            public void onFailure(String msg) {
                if(isAdded()) {
                    requireActivity().runOnUiThread(() -> {
                        Log.e("PostList", msg);
                        Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

        editPost = view.findViewById(R.id.edit_post);
        btnSend = view.findViewById(R.id.btn_send);

        LinearLayout advancedOptions = view.findViewById(R.id.advanced_options);
        editPost.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                advancedOptions.setVisibility(View.VISIBLE); // 🔥 显示选项
            }
        });

        switchAnonymous = view.findViewById(R.id.switch_friend_only);


        btnSend.setOnClickListener(v -> {
            String content = editPost.getText().toString().trim();
            if (!content.isEmpty()) {
                Post newPost = new Post();
                newPost.setUid(JwtManager.getGlobalUid(SposocApplication.getContext()));
                newPost.setContent(content);
                newPost.setFriend_only(switchAnonymous.isChecked());

                dao.addPost(newPost, new SCallback<Long>() {
                    @Override
                    public void onSuccess(Long pid) {
                        newPost.setPid(pid);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        newPost.setTime(sdf.format(System.currentTimeMillis()));
                        postList.add(0, newPost); // 最新的放前面
                        if(isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                adapter.notifyItemInserted(0);
                                recyclerView.scrollToPosition(0);
                                editPost.setText(""); // 清空输入框
                                advancedOptions.setVisibility(View.GONE); // 隐藏选项
                            });
                        }
                    }

                    @Override
                    public void onFailure(String msg) {
                        if(isAdded()) {
                            requireActivity().runOnUiThread(() -> {
                                Log.e("AddPost", msg);
                                Toast.makeText(view.getContext(), msg, Toast.LENGTH_SHORT).show();
                            });
                        }
                    }
                });
            }
        });

        return view;
    }
}