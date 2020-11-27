package com.schedulemate.schedulemate_user.ui.timetable.subjectList;

import java.io.Serializable;
import java.util.HashMap;

public class Subject {
    public static class SubjectItem implements Serializable {
        public final String id;
        public final String title;
        public final String dependency;
        public final String classification;
        public final String grade;
        public final String credit;
        public final HashMap<String, String> classes;

        public SubjectItem(String id, String title, String dependency, String classification, String grade, String credit, HashMap<String, String> classes) {
            this.id = id;
            this.title = title;
            this.dependency = dependency;
            this.classification = classification;
            this.grade = grade;
            this.credit = credit;
            this.classes = classes;
        }

        @Override
        public String toString() {
            return title;
        }
    }
}
