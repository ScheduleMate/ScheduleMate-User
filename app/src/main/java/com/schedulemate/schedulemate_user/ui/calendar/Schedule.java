package com.schedulemate.schedulemate_user.ui.calendar;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Schedule implements Serializable {
    private String id;
    private String startTime;
    private String endTime;
    private String title;
    private String memo;

    public Schedule(String id, String startTime, String endTime, String title, String memo) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.title = title;
        this.memo = memo;
    }

    public String getId() {
        return id;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getTitle() {
        return title;
    }

    public String getMemo() {
        return memo;
    }

    @NonNull
    @Override
    public String toString() {
        return title + " : " + startTime + " ~ " + endTime + " - " + memo;
    }
}
