package com.schedulemate.schedulemate_user.ui.calendar;

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
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CalendarViewModel extends ViewModel {
    private MutableLiveData<HashMap> monthSchedule = new MutableLiveData<>();
    private HashMap<String, ArrayList> hashMapDaySchedule = new HashMap<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CalendarViewModel() {
        monthSchedule.setValue(hashMapDaySchedule);
    }

    public void setMonthSchedule(String month, String university){
        database.getReference("calendar").child(user.getUid()).child(month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                hashMapDaySchedule.clear();
                for(DataSnapshot child : snapshot.getChildren()){
                    ArrayList<Schedule> schedules = new ArrayList<>();
                    for(DataSnapshot schedule : child.getChildren()){
                        if(snapshot.exists()) {
                            schedules.add(
                                    new Schedule(schedule.getKey(), schedule.child("startTime").getValue(String.class), schedule.child("endTime").getValue(String.class),
                                            schedule.child("title").getValue(String.class), schedule.child("memo").getValue(String.class))
                            );
                        }
                    }
                    hashMapDaySchedule.put(child.getKey(), schedules);
                }
                monthSchedule.setValue(hashMapDaySchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public LiveData<HashMap> getMonthSchedule(){
        return monthSchedule;
    }
}