package org.fm.backend.dto;

import org.fm.backend.model.CourseSchedule;

import java.util.List;

public class CourseDTO {
    public String coursePhotoUrl;
    public String courseName;
    public String courseDescription;
    public String courseStartTime;
    public String courseEndTime;
    public String courseGrade;
    public Integer coursePrice;
    public String coachName;
    public String instructorHonors;
    public String iconURL;
    public String features;
    public String courseType;
    public Integer capacity;
    public Integer isBooked;
    public List<CourseSchedule> schedules;
}
