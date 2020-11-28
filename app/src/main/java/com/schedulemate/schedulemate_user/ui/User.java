package com.schedulemate.schedulemate_user.ui;

import java.io.Serializable;

public class User implements Serializable {
    private String id;
    private String name;
    private String nickName;
    private String major;
    private String university;

    public User(String id, String name, String nickName, String major, String university) {
        this.id = id;
        this.name = name;
        this.nickName = nickName;
        this.major = major;
        this.university = university;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickName() {
        return nickName;
    }

    public String getMajor() {
        return major;
    }

    public String getUniversity() {
        return university;
    }
}
