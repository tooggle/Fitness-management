package org.fm.backend.dto;

import org.fm.backend.model.Course;
import org.fm.backend.model.CourseSchedule;

import java.util.List;

public class CourseRequest {
    public Course course;
    public List<CourseSchedule> courseSchedules;
}
