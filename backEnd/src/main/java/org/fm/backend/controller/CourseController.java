package org.fm.backend.controller;

import org.apache.ibatis.jdbc.Null;
import org.fm.backend.dto.CourseRequest;
import org.fm.backend.model.Course;
import org.fm.backend.service.CourseServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/Course")
public class CourseController {
    @Autowired
    CourseServie courseServie;

    @PostMapping("/PublishCourse")
    public String PublishCourse(String token, @RequestBody CourseRequest request) {
        if(request== null || request.course == null || request.courseSchedules == null){
            return "Invalid input";
        }
        return courseServie.publishCourse(token,request.course,request.courseSchedules);
    }
}
