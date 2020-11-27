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
    private final String userId;
    private String university = "한성대학교"; //TODO: 학교 선택 자동화
    private String semester = "2020-2"; //TODO: 현재 학기 자동화
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private MutableLiveData<ArrayList> semesterList = new MutableLiveData<>();
    private ArrayList<String> semesters = new ArrayList<>();

    public SharedViewModel() {
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        database.getReference("user").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                university = snapshot.child("university").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        semesterList.setValue(semesters);
    }

    public void setSemesterList(DatabaseReference dr){
        semesters.clear();
        dr.addChildEventListener(new ChildEventListener() {
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
        return userId;
    }

    public String getUniversity() {
        return university;
    }

    public LiveData<ArrayList> getSemesterList(){
        return semesterList;
    }
    
    //TODO:동기화

    public DatabaseReference getUserInfo(){
        return database.getReference("user").child(userId);
    }

    public DatabaseReference getSemesterDR(){
        return database.getReference(university).child(semester);
    }

    public DatabaseReference getSemesterInfoDR(){
        return database.getReference(university).child("info").child("semester");
    }

    public DatabaseReference getTimetableDR(){
        return database.getReference(university).child("timetable").child(userId);
    }

    public DatabaseReference getCalendarDR(){
        return database.getReference("calendar").child(userId);
    }
}
