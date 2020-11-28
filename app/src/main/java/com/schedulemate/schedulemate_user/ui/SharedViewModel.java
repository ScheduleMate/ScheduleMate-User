package com.schedulemate.schedulemate_user.ui;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.schedulemate.schedulemate_user.NavigationActivity;

import java.util.ArrayList;

public class SharedViewModel extends ViewModel {
    private User user;
    private String semester;
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private MutableLiveData<ArrayList> semesterList = new MutableLiveData<>();
    private ArrayList<String> semesters = new ArrayList<>();

    public SharedViewModel(User user, String semester) {
        this.user = user;
        this.semester = semester;
        semesterList.setValue(semesters);
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public void setSemesterList(DatabaseReference dr){
        semesters.clear();
        semesterList.setValue(semesters);
        dr.orderByKey().addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                semesters.add(snapshot.getKey());
                semesterList.setValue(semesters);
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

    public String getUserId(){
        return user.getId();
    }

    public String getUserNickName(){
        return user.getNickName();
    }

    public String getUniversity() {
        return user.getUniversity();
    }

    public String getSemester() {
        return semester;
    }

    public LiveData<ArrayList> getSemesterList(){
        return semesterList;
    }

    public DatabaseReference getUserInfoDR(){
        return database.getReference("user").child(user.getId());
    }

    public DatabaseReference getSemesterDR(){
        return database.getReference(user.getUniversity()).child(semester);
    }

    public DatabaseReference getSemesterInfoDR(){
        return database.getReference(user.getUniversity()).child("info").child("semester");
    }

    public DatabaseReference getTimetableDR(){
        return database.getReference(user.getUniversity()).child("timetable").child(user.getId());
    }

    public DatabaseReference getCalendarDR(){
        return database.getReference("calendar").child(user.getId());
    }
}
