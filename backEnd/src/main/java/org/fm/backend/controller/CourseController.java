package org.fm.backend.controller;

import org.fm.backend.dto.*;
import org.fm.backend.model.Course;
import org.fm.backend.model.Trainee;
import org.fm.backend.service.CourseServie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PostMapping("/ModifyCourse")
    public String ModifyCourse(String token, @RequestBody CourseRequest request) {
        if(request== null || request.course == null || request.courseSchedules == null){
            return "Invalid input";
        }
        return courseServie.modifyCourse(token,request.course,request.courseSchedules);
    }

    @DeleteMapping("/DeleteCourseByClassID")
    public String DeleteCourseByClassID(String token, int classID){
        return courseServie.deleteCourseByClassID(token,classID);
    }

    @GetMapping("/GetCourseByClassID")
    public Course GetCourseByClassID(String token, int classID){
        return courseServie.getCourseByClassID(token,classID);
    }

    @GetMapping("/GetAllCourse")
    public List<CourseDTO> GetAllCourse(String token){
        return courseServie.getAllCourse(token);
    }

    @GetMapping("/GetAllTraineesByClassID")
    public List<Trainee> GetAllTraineesByClassID(String token, int classID){
        return courseServie.getAllTraineesByClassID(token,classID);
    }

    @PostMapping("/ReserveCourse")
    public BookDTO ReserveCourse(@RequestBody ReserveCourseRequest request) {
        return courseServie.reserveCourse(request.token,request.classID, request.payMethod);
    }

    @PostMapping("/PayCourseFare")
    public String PayCourseFare(@RequestBody PayFareRequest request) {
        return courseServie.payCourseFare(request.token,request.bookID,request.payMethod);
    }

    @GetMapping("/GetReservedCourseByUserID")
    public List<courseDetail> GetReservedCourseByUserID(String token){
        return courseServie.getReservedCourseByUserID(token);
    }

}
