package com.schedulemate.schedulemate_user.ui.timetable;

import com.schedulemate.schedulemate_user.ui.timetable.classDetail.ClassGroup;
import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.RegisterSubject;
import com.schedulemate.schedulemate_user.ui.timetable.subjectList.Subject;

public class TimetableCell {
    private String title;
    private String day;
    private String start;
    private String end;
    private Subject.SubjectItem subjectItem;
    private ClassGroup classGroup;
    private RegisterSubject registerSubject;

    public TimetableCell(String title, String day, String start, String end, Subject.SubjectItem subjectItem, ClassGroup classGroup, RegisterSubject registerSubject) {
        this.title = title;
        this.day = day;
        this.start = start;
        this.end = end;
        this.subjectItem = subjectItem;
        this.classGroup = classGroup;
        this.registerSubject = registerSubject;
    }

    public String getTitle() {
        return title;
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

    public Subject.SubjectItem getSubjectItem() {
        return subjectItem;
    }

    public ClassGroup getClassGroup() {
        return classGroup;
    }

    public RegisterSubject getRegisterSubject() {
        return registerSubject;
    }
}
