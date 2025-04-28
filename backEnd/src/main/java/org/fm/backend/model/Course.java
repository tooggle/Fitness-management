package org.fm.backend.model;

import java.util.Date;

public class Course {
    private int classID;
    private int typeID;
    private String courseName;
    private int Capacity;
    private String courseDescription;
    private int coursePrice;
    private Date courseStartTime;
    private Date courseEndTime;
    private int courseLastDays;
    private float courseGrade;
    private String coursePhotoUrl;
    private String courseVideoUrl;
    private String features;

    public int getCoursePrice() {
        return coursePrice;
    }

    public void setCoursePrice(int coursePrice) {
        this.coursePrice = coursePrice;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public int getCapacity() {
        return Capacity;
    }

    public void setCapacity(int capacity) {
        Capacity = capacity;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public Date getCourseStartTime() {
        return courseStartTime;
    }

    public void setCourseStartTime(Date courseStartTime) {
        this.courseStartTime = courseStartTime;
    }

    public Date getCourseEndTime() {
        return courseEndTime;
    }

    public void setCourseEndTime(Date courseEndTime) {
        this.courseEndTime = courseEndTime;
    }

    public int getCourseLastDays() {
        return courseLastDays;
    }

    public void setCourseLastDays(int courseLastDays) {
        this.courseLastDays = courseLastDays;
    }

    public float getCourseGrade() {
        return courseGrade;
    }

    public void setCourseGrade(float courseGrade) {
        this.courseGrade = courseGrade;
    }

    public String getCoursePhotoUrl() {
        return coursePhotoUrl;
    }

    public void setCoursePhotoUrl(String coursePhotoUrl) {
        this.coursePhotoUrl = coursePhotoUrl;
    }

    public String getCourseVideoUrl() {
        return courseVideoUrl;
    }

    public void setCourseVideoUrl(String courseVideoUrl) {
        this.courseVideoUrl = courseVideoUrl;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    @Override
    public String toString() {
        return "Course{" +
                "classID=" + classID +
                ", typeID=" + typeID +
                ", courseName='" + courseName + '\'' +
                ", Capacity=" + Capacity +
                ", courseDescription='" + courseDescription + '\'' +
                ", coursePrice=" + coursePrice +
                ", courseStartTime=" + courseStartTime +
                ", courseEndTime=" + courseEndTime +
                ", courseLastDays=" + courseLastDays +
                ", courseGrade=" + courseGrade +
                ", coursePhotoUrl='" + coursePhotoUrl + '\'' +
                ", courseVideoUrl='" + courseVideoUrl + '\'' +
                ", features='" + features + '\'' +
                '}';
    }
}
