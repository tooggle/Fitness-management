package org.fm.backend.model;

public class CourseSchedule {
    private int classID ;
    private int dayOfWeek ;
    private String classTime ;

    public String getClassTime() {
        return classTime;
    }

    public void setClassTime(String classTime) {
        this.classTime = classTime;
    }

    public int getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    @Override
    public String toString() {
        return "CourseSchedule{" +
                "classID=" + classID +
                ", dayOfWeek=" + dayOfWeek +
                ", classTime='" + classTime + '\'' +
                '}';
    }
}
