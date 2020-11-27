package com.schedulemate.schedulemate_user.ui.timetable.classDetail;

public class ClassGroup {
    private String subjectId;
    private String classId;

    public ClassGroup(String subjectId, String classId) {
        this.subjectId = subjectId;
        this.classId = classId;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public String getClassId() {
        return classId;
    }
}
