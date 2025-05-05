package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.CourseSchedule;

import java.util.List;

@Mapper
public interface CourseScheduleMapper {

    // 插入课程时间表
    @Insert("""
        INSERT INTO CourseSchedule (classID, dayOfWeek, classTime)
        VALUES (#{classID}, #{dayOfWeek}, #{classTime})
        """)
    boolean insertCourseSchedule(CourseSchedule courseSchedule);

    // 按 classID 删除课程时间表
    @Delete("DELETE FROM CourseSchedule WHERE classID = #{classID}")
    boolean deleteCourseSchedulesByClassID(@Param("classID") int classID);

    // 按 classID 查询课程时间表
    @Select("SELECT classID, dayOfWeek, classTime FROM CourseSchedule WHERE classID = #{classID}")
    List<CourseSchedule> getCourseSchedulesByClassID(@Param("classID") int classID);
}
