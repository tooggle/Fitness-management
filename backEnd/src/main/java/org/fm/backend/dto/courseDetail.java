package org.fm.backend.dto;

import org.fm.backend.model.CourseSchedule;

import java.util.List;

public class courseDetail {
    public String coursePhotoUrl;
    public String courseName;
    public String courseStartTime;
    public String courseEndTime;
    public int coursePrice;
    public int classID;
    public int bookID;
    public List<CourseSchedule> schedules;
}
