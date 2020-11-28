package com.schedulemate.schedulemate_user.ui.community.post;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.community.Comment;
import com.schedulemate.schedulemate_user.ui.community.CommunityViewModel;
import com.schedulemate.schedulemate_user.ui.community.Post;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PostFragment extends Fragment {
    private CommunityViewModel communityViewModel;
    private SharedViewModel sharedViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        communityViewModel = new ViewModelProvider(requireActivity()).get(CommunityViewModel.class);
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        Post post = PostFragmentArgs.fromBundle(getArguments()).getPost();

        TextView textViewPostWriter = view.findViewById(R.id.textViewPostWriter);
        TextView textViewPostTime = view.findViewById(R.id.textViewPostTime);
        TextView textViewPostTitle = view.findViewById(R.id.textViewPostTitle);
        TextView textViewPostContent = view.findViewById(R.id.textViewPostContent);
        TextView textViewCommentCount = view.findViewById(R.id.textViewCommentCount);
        EditText editTextNewComment = view.findViewById(R.id.editTextNewComment);

        textViewPostWriter.setText(post.getWriterNickName());
        textViewPostTime.setText(post.getTime());
        textViewPostTitle.setText(post.getTitle());
        textViewPostContent.setText(post.getContent());
        textViewCommentCount.setText("(" + (post.getComments()).size() + ")");

        PostRecyclerViewAdapter adapter = new PostRecyclerViewAdapter(post.getComments(), communityViewModel.getClassId().getValue(), post.getId());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewComment);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        communityViewModel.setCommentList(post.getId());

        communityViewModel.getCommentList().observe(getViewLifecycleOwner(), new Observer<List<Comment>>() {
            @Override
            public void onChanged(List<Comment> comments) {
                adapter.setItems(comments);
                adapter.notifyDataSetChanged();
                textViewCommentCount.setText("(" + comments.size() + ")");
            }
        });

        view.findViewById(R.id.buttonCommentSubmit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextNewComment.getText().toString().trim() != ""){
                    Date now = new Date(System.currentTimeMillis());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                    DatabaseReference dr = FirebaseDatabase.getInstance().getReference("community").child(communityViewModel.getClassId().getValue())
                            .child("post").child(post.getId()).child("comment").push();
                    dr.child("content").setValue(editTextNewComment.getText().toString());
                    dr.child("writer").setValue(sharedViewModel.getUserId());
                    dr.child("writerNickName").setValue(sharedViewModel.getUserNickName());
                    dr.child("time").setValue(simpleDateFormat.format(now), new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            communityViewModel.setCommentList(post.getId());
                        }
                    });

                    editTextNewComment.setText("");
                }
            }
        });

        ImageButton imageButtonPostDelete = view.findViewById(R.id.imageButtonPostDelete);
        ImageButton imageButtonPostEdit = view.findViewById(R.id.imageButtonPostEdit);

        if(sharedViewModel.getUserId().equals(post.getWriter())) {
            imageButtonPostDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("게시글 삭제").setMessage("정말 이 게시글을 삭제하시겠습니까?").setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            FirebaseDatabase.getInstance().getReference("community").child(communityViewModel.getClassId().getValue())
                                    .child("post").child(post.getId()).removeValue();
                            Navigation.findNavController(view).navigate(R.id.action_postFragment_to_nav_community);
                        }
                    }).setNegativeButton("취소", null);

                    builder.show();
                }
            });

            imageButtonPostEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostFragmentDirections.ActionPostFragmentToPostingFragment action = PostFragmentDirections.actionPostFragmentToPostingFragment();
                    action.setPost(post);
                    Navigation.findNavController(v).navigate(action);
                }
            });
        }
        else{
            imageButtonPostDelete.setVisibility(View.GONE);
            imageButtonPostEdit.setVisibility(View.GONE);
        }

        return view;
    }
}