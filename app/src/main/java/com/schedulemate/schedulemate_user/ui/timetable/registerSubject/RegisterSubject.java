package com.schedulemate.schedulemate_user.ui.timetable.registerSubject;

import java.io.Serializable;
import java.util.ArrayList;

public class RegisterSubject implements Serializable {
    private String id;
    private String title;
    private String professor;
    private ArrayList<Time> times = new ArrayList<>();

    public RegisterSubject(String id, String title, String professor) {
        this.id = id;
        this.title = title;
        this.professor = professor;
    }

    public void addTime(Time time){
        times.add(time);
    }

    public boolean deleteTime(Time time){
        return times.remove(time);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getProfessor() {
        return professor;
    }

    public ArrayList<Time> getTimes() {
        return times;
    }

    static public class Time implements Serializable{
        private String day;
        private String start;
        private String end;
        private String place;

        public Time(String day, String start, String end, String place) {
            this.day = day;
            this.start = start;
            this.end = end;
            this.place = place;
        }

        public String getDay() {
            return day;
        }

        public String getStart() {
            return start;
        }

        public String getEnd() {
            return end;
        }

        public String getPlace() {
            return place;
        }
    }
}
