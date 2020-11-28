package com.schedulemate.schedulemate_user.ui.community;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunityViewModel extends ViewModel {
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private MutableLiveData<HashMap> boardMap = new MutableLiveData<>();
    private HashMap<String, String> boardTitles = new HashMap<>();

    private MutableLiveData<String> classId = new MutableLiveData<>();

    private MutableLiveData<List<Post>> postList = new MutableLiveData<>();
    private ArrayList<Post> posts = new ArrayList<>();

    private MutableLiveData<List<Comment>> commentList = new MutableLiveData<>();
    private ArrayList<Comment> comments = new ArrayList<>();

    public CommunityViewModel() {
        boardMap.setValue(boardTitles);
        postList.setValue(posts);
        commentList.setValue(comments);
    }

    public void setBoardMap(String university, String semester){
        boardTitles.clear();
        boardMap.setValue(boardTitles);
        FirebaseDatabase.getInstance().getReference(university).child("timetable").child(user.getUid()).child(semester).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(!snapshot.hasChild("title")) {
                    FirebaseDatabase.getInstance().getReference(university).child(semester).child("classInfo").child(snapshot.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            boardTitles.put(snapshot.getKey(), snapshot.child("title").getValue(String.class));
                            boardMap.setValue(boardTitles);
                            if (classId.getValue() == null) setClassId(snapshot.getKey());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setClassId(String classId) {
        this.classId.setValue(classId);
        setPostList();
    }

    public void setPostList() {
        posts.clear();
        postList.setValue(posts);
        FirebaseDatabase.getInstance().getReference("community").child(classId.getValue()).child("post").orderByChild("time").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()) {
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String content = snapshot.child("content").getValue(String.class);
                    String writerNickName = snapshot.child("writerNickName").getValue(String.class);
                    String writer = snapshot.child("writer").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);

                    ArrayList<Comment> comments = new ArrayList<>();

                    for (DataSnapshot child : snapshot.child("comment").getChildren()) {
                        String cId = child.getKey();
                        String cContent = child.child("content").getValue(String.class);
                        String cWriterNickName = child.child("writerNickName").getValue(String.class);
                        String cWriter = child.child("writer").getValue(String.class);
                        String cTime = child.child("time").getValue(String.class);

                        comments.add(new Comment(cId, cContent, cWriterNickName, cWriter, cTime));
                    }
                    posts.add(new Post(id, title, content, writerNickName, writer, time, comments));
                    postList.setValue(posts);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(Post post : posts){
                    if(post.getId().equals(snapshot.getKey())){
                        post.setTitle(snapshot.child("title").getValue(String.class));
                        post.setContent(snapshot.child("content").getValue(String.class));
                        post.setWriterNickName(snapshot.child("writerNickName").getValue(String.class));
                        post.setWriter(snapshot.child("writer").getValue(String.class));
                        post.setTime(snapshot.child("time").getValue(String.class));

                        post.getComments().clear();

                        for (DataSnapshot child : snapshot.child("comment").getChildren()) {
                            String cId = child.getKey();
                            String cContent = child.child("content").getValue(String.class);
                            String cWriterNickName = child.child("writerNickName").getValue(String.class);
                            String cWriter = child.child("writer").getValue(String.class);
                            String cTime = child.child("time").getValue(String.class);

                            post.getComments().add(new Comment(cId, cContent, cWriterNickName, cWriter, cTime));
                        }
                        postList.setValue(posts);
                        break;
                    }
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for(Post post : posts){
                    if(post.getId().equals(snapshot.getKey())){
                        posts.remove(post);
                        postList.setValue(posts);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setCommentList(String postId) {
        comments.clear();
        commentList.setValue(comments);
        FirebaseDatabase.getInstance().getReference("community").child(classId.getValue()).child("post").child(postId).child("comment").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                    String id = snapshot.getKey();
                    String content = snapshot.child("content").getValue(String.class);
                    String writerNickName = snapshot.child("writerNickName").getValue(String.class);
                    String writer = snapshot.child("writer").getValue(String.class);
                    String time = snapshot.child("time").getValue(String.class);

                    if(content != null && writerNickName != null && writer != null && time != null) {
                        Comment comment = new Comment(id, content, writerNickName, writer, time);
                        comments.add(comment);
                        commentList.setValue(comments);
                    }
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                for(Comment comment : comments){
                    if(comment.getId().equals(snapshot.getKey())){
                        comments.remove(comment);
                        commentList.setValue(comments);
                        break;
                    }
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<HashMap> getBoardMap() {
        return boardMap;
    }

    public LiveData<String> getClassId(){
        return classId;
    }

    public LiveData<List<Post>> getPostList(){
        return postList;
    }

    public LiveData<List<Comment>> getCommentList(){
        return commentList;
    }
}