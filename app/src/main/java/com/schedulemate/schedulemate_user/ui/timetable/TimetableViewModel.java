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
import com.google.firebase.database.ValueEventListener;
import com.schedulemate.schedulemate_user.ui.SharedViewModel;
import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.RegisterSubject;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimetableViewModel extends ViewModel {
    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private MutableLiveData<ArrayList> timetableList = new MutableLiveData<>();
    private ArrayList<String> timetables = new ArrayList<>();

    private MutableLiveData<HashMap> timetable = new MutableLiveData<>();
    private HashMap<String, String> timetableSubjects = new HashMap<>();

    private MutableLiveData<ArrayList> registeredTimetable = new MutableLiveData<>();
    private ArrayList<RegisterSubject> registeredTimetableSubjects = new ArrayList<>();

    private MutableLiveData<ArrayList> subjectList = new MutableLiveData<>();
    private ArrayList<Subject.SubjectItem> subjectItems = new ArrayList<>();

    private MutableLiveData<ArrayList> classGroupList = new MutableLiveData<>();
    private ArrayList<ClassGroup> classGroups = new ArrayList<>();

    private String semester;
    private String timetableStart;
    private String timetableEnd;

    public TimetableViewModel(){
        subjectList.setValue(subjectItems);
        timetableList.setValue(timetables);
        timetable.setValue(timetableSubjects);
        registeredTimetable.setValue(registeredTimetableSubjects);
        timetableStart = "09:00";
        timetableEnd = "15:00";
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
        registeredTimetableSubjects.clear();
        dr.child(semester).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.hasChild("title")){
                    String id = snapshot.getKey();
                    String title = snapshot.child("title").getValue(String.class);
                    String professor = snapshot.child("professor").getValue(String.class);

                    RegisterSubject rs = new RegisterSubject(id, title, professor);

                    for(DataSnapshot child : snapshot.child("time").getChildren()){
                        String day = child.child("day").getValue(String.class);
                        String start = child.child("start").getValue(String.class);
                        String end = child.child("end").getValue(String.class);
                        String place = child.child("place").getValue(String.class);

                        RegisterSubject.Time time = new RegisterSubject.Time(day, start, end, place);
                        rs.addTime(time);
                    }
                    registeredTimetableSubjects.add(rs);
                    registeredTimetable.setValue(registeredTimetableSubjects);
                }
                else{
                    String subjectId = snapshot.getKey();
                    String classId = snapshot.getValue(String.class);

                    timetableSubjects.put(subjectId, classId);
                    timetable.setValue(timetableSubjects);
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

    public void setSubjectList(String univ){
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

    public void setClassGroupList(String university){
        classGroups.clear();
        database.getReference(university).child(semester).child("classInfo").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String classId = snapshot.getKey();
                String subjectTitle = snapshot.child("title").getValue(String.class);
                String professor = snapshot.child("professor").getValue(String.class);
                String type = snapshot.child("type").getValue(String.class);

                ClassGroup classGroup = new ClassGroup(classId, subjectTitle, professor, type);

                for(DataSnapshot child : snapshot.child("time").getChildren()){
                    String day = child.child("day").getValue(String.class);
                    String start = child.child("start").getValue(String.class);
                    String end = child.child("end").getValue(String.class);
                    String place = child.child("place").getValue(String.class);

                    RegisterSubject.Time time = new RegisterSubject.Time(day, start, end, place);
                    classGroup.addTime(time);
                }

                if(snapshot.hasChild("schedule/midExam")){
                    snapshot.getRef().child("schedule").child("midExam").orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot midExam = snapshot.getChildren().iterator().next();
                            String startTime = midExam.child("startTime").getValue(String.class);
                            String endTime = midExam.child("endTime").getValue(String.class);
                            String place = midExam.child("place").getValue(String.class);
                            String registrant = midExam.child("registrant").getValue(String.class);
                            String registeredTime = midExam.getKey();

                            classGroup.setMidExam(new ClassGroup.Exam(startTime, endTime, place, registrant, registeredTime));
                            classGroupList.setValue(classGroups);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if(snapshot.hasChild("schedule/finalExam")){
                    snapshot.getRef().child("schedule").child("finalExam").orderByKey().addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            DataSnapshot finalExam = snapshot.getChildren().iterator().next();
                            String startTime = finalExam.child("startTime").getValue(String.class);
                            String endTime = finalExam.child("endTime").getValue(String.class);
                            String place = finalExam.child("place").getValue(String.class);
                            String registrant = finalExam.child("registrant").getValue(String.class);
                            String registeredTime = finalExam.getKey();

                            classGroup.setFinalExam(new ClassGroup.Exam(startTime, endTime, place, registrant, registeredTime));
                            classGroupList.setValue(classGroups);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

                if(snapshot.hasChild("schedule/homeWork")){
                    snapshot.getRef().child("schedule").child("homeWork").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String timeLimit = snapshot.child("timeLimit").getValue(String.class);
                            String content = snapshot.child("content").getValue(String.class);
                            String registrant = snapshot.child("registrant").getValue(String.class);
                            String registeredTime = snapshot.getKey();

                            classGroup.addHomeWorks(new ClassGroup.HomeWork(timeLimit, content, registrant, registeredTime));
                            classGroupList.setValue(classGroups);
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
                classGroups.add(classGroup);
                classGroupList.setValue(classGroups);
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

    public String getSemester(){
        return semester;
    }

    public LiveData<ArrayList> getTimetableList() {
        return timetableList;
    }

    public LiveData<HashMap> getUserTimetable(){
        return timetable;
    }

    public LiveData<ArrayList> getSubjectList() {
        return subjectList;
    }

    public LiveData<ArrayList> getClassGroupList() {
        return classGroupList;
    }

    public String getTimetableStart() {
        return timetableStart;
    }

    public String getTimetableEnd() {
        return timetableEnd;
    }

    public int getStartTime(){
        Pattern pattern = Pattern.compile("([0-9]{2}):([0-9]{2})");
        Matcher start = pattern.matcher(timetableStart);

        if(start.find()){
            return Integer.parseInt(start.group(1));
        }
        else
            return 0;
    }

    public int getEndTime(){
        Pattern pattern = Pattern.compile("([0-9]{2}):([0-9]{2})");
        Matcher end = pattern.matcher(timetableEnd);

        if(end.find()){
            return Integer.parseInt(end.group(1));
        }
        else
            return 0;
    }
}