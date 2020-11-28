package com.schedulemate.schedulemate_user.ui.community;

import java.io.Serializable;

public class Comment implements Serializable {
    private String id;
    private String content;
    private String writerNickName;
    private String writer;
    private String time;

    public Comment(String id, String content, String writerNickName, String writer, String time) {
        this.id = id;
        this.content = content;
        this.writerNickName = writerNickName;
        this.writer = writer;
        this.time = time;
    }

    public String getId() {
        return id;
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

    @Override
    public String toString() {
        return "Comment{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", writerNickName='" + writerNickName + '\'' +
                ", writer='" + writer + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
