package org.fm.backend.service;

import jakarta.annotation.PostConstruct;
import org.fm.backend.dao.CourseMapper;
import org.fm.backend.dao.CourseScheduleMapper;
import org.fm.backend.dao.TeachesMapper;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.model.Course;
import org.fm.backend.model.CourseSchedule;
import org.fm.backend.model.Teaches;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Objects;

@Service
public class CourseServie {

    @Autowired
    JWTHelper jwtHelper;

    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseScheduleMapper courseScheduleMapper;

    @Autowired
    TeachesMapper teachesMapper;

    public String publishCourse(String token, Course course, List<CourseSchedule> courseSchedules){
        TokenValidationResult result = jwtHelper.validateToken(token);
        if(!Objects.equals(result.Role, "coach")){
            return "身份权限不符！";
        }
        if(!courseMapper.insert(course)){
            return "课程发布失败：无法插入课程信息";
        }
        int classID = course.getClassID();
        Teaches teaches = new Teaches();
        teaches.setCoachID(result.userID);
        teaches.setClassID(classID);
        teaches.setTypeID(course.getTypeID());
        if(!teachesMapper.insert(teaches)){
            return "课程发布失败：无法插入教练授课信息";
        }
        for (CourseSchedule schedule : courseSchedules) {
            schedule.setClassID(classID); // 设置关联的课程ID
            if (!courseScheduleMapper.insertCourseSchedule(schedule)) {
                return "课程发布失败: 无法插入授课时间信息";
            }
        }
        return "课程发布成功";
    }

}
