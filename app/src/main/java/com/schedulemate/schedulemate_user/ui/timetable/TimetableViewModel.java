package com.schedulemate.schedulemate_user.ui.timetable;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.util.ArrayList;
import java.util.HashMap;

public class TimetableViewModel extends ViewModel {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();
    private MutableLiveData<ArrayList> timetableList = new MutableLiveData<>();
    private ArrayList<String> timetables = new ArrayList<>();
    private MutableLiveData<ArrayList> timetable = new MutableLiveData<>();
    private ArrayList<Subject.SubjectItem> timetableSubjects = new ArrayList<>();
    private MutableLiveData<ArrayList> subjectList = new MutableLiveData<>();
    private ArrayList<Subject.SubjectItem> subjectItems = new ArrayList<>();
    private String semester;

    public TimetableViewModel(){
        subjectList.setValue(subjectItems);
        timetableList.setValue(timetables);
        timetable.setValue(timetableSubjects);
    }

    public void setSemester(String semester){
        this.semester = semester;
    }

    public void setTimetableList(DatabaseReference dr){
        timetables.clear();
        dr.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                timetables.add(snapshot.getKey());
                timetableList.setValue(timetables);
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

    public void setTimetable(DatabaseReference dr){
        timetableSubjects.clear();
        dr.child(semester).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

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

    public void setSubjectList(String univ, String semester){
        subjectItems.clear();
        database.getReference(univ).child(semester).child("subject").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String id = snapshot.getKey();
                String title = snapshot.child("title").getValue().toString();
                String dependency = snapshot.child("dependency").getValue().toString();
                String classification = snapshot.child("classification").getValue().toString();
                String grade = snapshot.child("grade").getValue().toString();
                String credit = snapshot.child("credit").getValue().toString();
                HashMap<String, String> classes = (HashMap)snapshot.child("class").getValue();
                Subject.SubjectItem subjectItem = new Subject.SubjectItem(id, title, dependency, classification, grade, credit, classes);
                subjectItems.add(subjectItem);
                subjectList.setValue(subjectItems);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                //itemsMap.get(snapshot.getKey()).replace("title", snapshot.getValue().toString());
                //TODO: 리스트 새로고침
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                //itemsMap.remove(snapshot.getKey());
                //TODO: 리스트 새로고침
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public String getSemester(){
        return semester;
    }

    public LiveData<ArrayList> getTimetableList() {
        return timetableList;
    }

    public LiveData<ArrayList> getUserTimetable(){
        return timetable;
    }

    public LiveData<ArrayList> getSubjectList() {
        return subjectList;
    }
}