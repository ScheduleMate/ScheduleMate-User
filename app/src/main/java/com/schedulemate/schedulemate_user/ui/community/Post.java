package com.schedulemate.schedulemate_user.ui.community;

import java.io.Serializable;
import java.util.ArrayList;

public class Post implements Serializable {
    private String id;
    private String title;
    private String content;
    private String writerNickName;
    private String writer;
    private String time;
    private ArrayList<Comment> comments;

    public Post(String id, String title, String content, String writerNickName, String writer, String time, ArrayList<Comment> comments) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.writerNickName = writerNickName;
        this.writer = writer;
        this.time = time;
        this.comments = comments;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setWriterNickName(String writerNickName) {
        this.writerNickName = writerNickName;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getWriterNickName() {
        return writerNickName;
    }

    public String getWriter() {
        return writer;
    }

    public String getTime() {
        return time;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }
}
