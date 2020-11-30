package com.schedulemate.schedulemate_user.ui.community.posting;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.R;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.community.CommunityViewModel;
import com.schedulemate.schedulemate_user.ui.community.Post;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostingFragment extends Fragment {
    private SharedViewModel sharedViewModel;
    private CommunityViewModel communityViewModel;
    private String classTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        communityViewModel = new ViewModelProvider(requireActivity()).get(CommunityViewModel.class);

        Post post = PostingFragmentArgs.fromBundle(getArguments()).getPost();
        classTitle = PostingFragmentArgs.fromBundle(getArguments()).getClassTitle();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posting, container, false);

        EditText editTextPostTitle = view.findViewById(R.id.editTextPostTitle);
        EditText editTextPostContent = view.findViewById(R.id.editTextPostContent);

        if (post != null){
            editTextPostTitle.setText(post.getTitle());
            editTextPostContent.setText(post.getContent());
        }

        Button buttonPost = view.findViewById(R.id.buttonPost);

        buttonPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date now = new Date(System.currentTimeMillis());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                String title = editTextPostTitle.getText().toString();
                String content = editTextPostContent.getText().toString();
                String writerNickName = sharedViewModel.getUserNickName();
                String writer = sharedViewModel.getUserId();
                String time = simpleDateFormat.format(now);

                DatabaseReference dr;
                if (post != null){
                    dr = FirebaseDatabase.getInstance().getReference("community").child(communityViewModel.getClassId().getValue()).child("post").child(post.getId());
                }
                else {
                    dr = FirebaseDatabase.getInstance().getReference("community").child(communityViewModel.getClassId().getValue()).child("post").push();
                }
                dr.child("title").setValue(title);
                dr.child("content").setValue(content);
                dr.child("writerNickName").setValue(writerNickName);
                dr.child("writer").setValue(writer);
                dr.child("time").setValue(time, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        communityViewModel.setPostList();
                    }
                });

                Post post = new Post(dr.getKey(), title, content, writerNickName, writer, time, new ArrayList<>());

                PostingFragmentDirections.ActionPostingFragmentToPostFragment action = PostingFragmentDirections.actionPostingFragmentToPostFragment(post, classTitle);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return view;
    }
}