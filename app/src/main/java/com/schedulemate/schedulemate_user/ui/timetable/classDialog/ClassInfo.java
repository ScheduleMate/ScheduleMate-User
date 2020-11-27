package com.schedulemate.schedulemate_user.ui.timetable.classDialog;

public class ClassInfo {
    private String time;
    private String place;

    public ClassInfo(String time, String place) {
        this.time = time;
        this.place = place;
    }

    @Override
    public String toString() {
        return "ClassInfo{" +
                "time='" + time + '\'' +
                ", place='" + place + '\'' +
                '}';
    }
}
