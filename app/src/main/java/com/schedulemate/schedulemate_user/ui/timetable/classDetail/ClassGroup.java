package com.schedulemate.schedulemate_user.ui.timetable.classDetail;

import com.schedulemate.schedulemate_user.ui.timetable.registerSubject.RegisterSubject;

import java.io.Serializable;
import java.util.ArrayList;

public class ClassGroup implements Serializable {
    private String classId;
    private String subjectTitle;
    private String professor;
    private String type;
    private ArrayList<RegisterSubject.Time> times = new ArrayList<>();
    private Exam midExam;
    private Exam finalExam;
    private ArrayList<HomeWork> homeWorks = new ArrayList<>();

    public ClassGroup(String classId, String subjectTitle, String professor, String type) {
        this.classId = classId;
        this.subjectTitle = subjectTitle;
        this.professor = professor;
        this.type = type;
    }

    public void addTime(RegisterSubject.Time time){
        times.add(time);
    }

    public boolean deleteTime(RegisterSubject.Time time){
        return times.remove(time);
    }

    public void setMidExam(Exam midExam) {
        this.midExam = midExam;
    }

    public void setFinalExam(Exam finalExam) {
        this.finalExam = finalExam;
    }

    public void setHomeWorks(ArrayList<HomeWork> homeWorks) {
        this.homeWorks = homeWorks;
    }

    public void addHomeWorks(HomeWork homework) {
        homeWorks.add(homework);
    }

    public boolean deleteHomeWork(HomeWork homeWork){
        return homeWorks.remove(homeWork);
    }

    public String getClassId() {
        return classId;
    }

    public String getSubjectTitle() {
        return subjectTitle;
    }

    public String getProfessor() {
        return professor;
    }

    public String getType() {
        return type;
    }

    public ArrayList<RegisterSubject.Time> getTimes() {
        return times;
    }

    public Exam getMidExam() {
        return midExam;
    }

    public Exam getFinalExam() {
        return finalExam;
    }

    public ArrayList<HomeWork> getHomeWorks() {
        return homeWorks;
    }

    public static class Exam implements Serializable{
        private String startTime;
        private String endTime;
        private String place;
        private String registrant;
        private String registeredTime;

        public Exam(String startTime, String endTime, String place, String registrant, String registeredTime) {
            this.place = place;
            this.startTime = startTime;
            this.endTime = endTime;
            this.registrant = registrant;
            this.registeredTime = registeredTime;
        }

        public String getPlace() {
            return place;
        }

        public String getStartTime() {
            return startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public String getRegistrant() {
            return registrant;
        }

        public String getRegisteredTime() {
            return registeredTime;
        }
    }

    public static class HomeWork implements Serializable{
        private String timeLimit;
        private String content;
        private String registrant;
        private String registeredTime;

        public HomeWork(String timeLimit, String content, String registrant, String registeredTime) {
            this.timeLimit = timeLimit;
            this.content = content;
            this.registrant = registrant;
            this.registeredTime = registeredTime;
        }

        public String getTimeLimit() {
            return timeLimit;
        }

        public String getContent() {
            return content;
        }

        public String getRegistrant() {
            return registrant;
        }

        public String getRegisteredTime() {
            return registeredTime;
        }
    }
}
