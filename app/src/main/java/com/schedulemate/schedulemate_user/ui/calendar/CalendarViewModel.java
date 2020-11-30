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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CalendarViewModel extends ViewModel {
    private MutableLiveData<HashMap> monthSchedule = new MutableLiveData<>();
    private HashMap<String, ArrayList> hashMapDaySchedule = new HashMap<>();
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public CalendarViewModel() {
        monthSchedule.setValue(hashMapDaySchedule);
    }

    public void setMonthSchedule(String month, String university, String uid, String semester){
        hashMapDaySchedule.clear();
        database.getReference("calendar").child(user.getUid()).child(month).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
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
                    if(hashMapDaySchedule.containsKey(child.getKey())){
                        hashMapDaySchedule.get(child.getKey()).addAll(schedules);
                    }
                    else {
                        hashMapDaySchedule.put(child.getKey(), schedules);
                    }
                }
                monthSchedule.setValue(hashMapDaySchedule);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        database.getReference(university).child("timetable").child(uid).child(semester).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if(!s.hasChild("title")){
                        database.getReference(university).child(semester).child("classInfo").child(s.getValue(String.class)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String title = snapshot.child("title").getValue(String.class);
                                for(DataSnapshot time : snapshot.child("time").getChildren()){
                                    String day = time.child("day").getValue(String.class);
                                    String end = time.child("end").getValue(String.class);
                                    String start = time.child("start").getValue(String.class);
                                    String place = time.child("place").getValue(String.class);

                                    Pattern pattern = Pattern.compile("([0-9]{4})-([0-9]{2})");
                                    Matcher matcher = pattern.matcher(month);
                                    if(matcher.find()) {
                                        Calendar calendar = new GregorianCalendar(Integer.parseInt(matcher.group(1)), Integer.parseInt(matcher.group(2)) - 1, 1);
                                        for (int i = 1; i <= calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
                                            try {
                                                if(getDateDay(String.format("%s-%02d", month, i), "yyyy-MM-dd").equals(day)){
                                                    if(!hashMapDaySchedule.containsKey(String.valueOf(i))){
                                                        hashMapDaySchedule.put(String.valueOf(i), new ArrayList());
                                                    }
                                                    hashMapDaySchedule.get(String.valueOf(i)).add(new Schedule(snapshot.getKey(), start, end, title, place));
                                                    monthSchedule.setValue(hashMapDaySchedule);
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public LiveData<HashMap> getMonthSchedule(){
        return monthSchedule;
    }

    public String getDateDay(String date, String dateType) throws Exception {
        String day = "" ;

        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;

        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);

        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;

        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;
        }

        return day ;
    }
}