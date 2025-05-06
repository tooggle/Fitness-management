package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.BookCourseInfo;
import org.fm.backend.dto.CourseInfo;
import org.fm.backend.model.Course;
import org.fm.backend.model.CourseSearchParams;

import java.util.List;

@Mapper
public interface CourseMapper {

    // 根据 classID 获取课程
    @Select("SELECT * FROM Course WHERE classID = #{classID}")
    Course getCourseByClassID(int classID);

    //根据 userID 获取所有已预约但未付款的课程
    @Select("SELECT classID, bookID, typeID, courseName, coursePrice, " +
            "courseStartTime, courseEndTime, coursePhotoUrl, payMethod, " +
            "bookStatus, bookTime FROM Course NATURAL JOIN Book " +
            "WHERE traineeID = #{userID} AND bookStatus = 1")
    List<BookCourseInfo> getReservedCourseByUserID(int userID);

    // 根据userID获取个人所有已付款（即已参与的课程）
    @Select("SELECT * FROM Course NATURAL JOIN Participate " +
            "WHERE traineeID = #{userID}")
    List<Course> getParticipatedCourseByUserID(int userID);

    // 获取教练本人所教的所有课程
    @Select("SELECT * FROM Course NATURAL JOIN Teaches " +
            "WHERE coachID = #{userID}")
    List<Course> getPublishedCourseByUserID(int userID);

    // 随机获取所有课程
    @Select("SELECT * FROM Course ORDER BY RAND()")
    List<Course> getAllCourseRandomly();

    // 插入课程
    @Insert({"INSERT INTO Course(typeID, courseName, Capacity, courseDescription, " +
            "coursePrice, courseStartTime, courseEndTime, courseLastDays, " +
            "courseGrade, coursePhotoUrl, courseVideoUrl, features) " +
            "VALUES(#{typeID}, #{courseName}, #{Capacity}, #{courseDescription}, " +
            "#{coursePrice}, #{courseStartTime}, #{courseEndTime}, #{courseLastDays}, " +
            "#{courseGrade}, #{coursePhotoUrl}, #{courseVideoUrl}, #{features})"})
    @Options(useGeneratedKeys = true, keyProperty = "classID", keyColumn = "classID")
    boolean insert(Course course);

    // 根据 classID 删除课程
    @Delete("DELETE FROM Course WHERE classID = #{classID}")
    boolean deleteByClassID(int classID);

    // 更新课程
    @Update({"UPDATE Course SET typeID=#{typeID}, courseName=#{courseName}, " +
            "Capacity=#{Capacity}, courseDescription=#{courseDescription}, " +
            "coursePrice=#{coursePrice}, courseStartTime=#{courseStartTime}, " +
            "courseEndTime=#{courseEndTime}, courseLastDays=#{courseLastDays}, " +
            "courseGrade=#{courseGrade}, coursePhotoUrl=#{coursePhotoUrl}, " +
            "courseVideoUrl=#{courseVideoUrl} WHERE classID=#{classID}"})
    boolean updateByClassID(Course course);

    // 获取课程的教练ID
    @Select("SELECT coachID FROM Teaches WHERE classID = #{classID}")
    Integer getCoachIDByClassID(int classID);

    // 根据类型ID获取类型名称
    @Select("SELECT typeName FROM CourseType WHERE typeID = #{typeID}")
    String getTypeNameByTypeID(int typeID);

    // 获取课程详细信息
    @Select("SELECT * FROM Course WHERE classID = #{classID}")
    Course getCourseDetails(int classID);

    //学员获取今日课程列表
    @Select({"<script>",
            "SELECT c.classID, c.courseName, c.courseDescription, c.coursePrice, ",
            "c.courseStartTime, c.courseEndTime, c.courseGrade, c.coursePhotoUrl, ",
            "cs.classTime ",
            "FROM Course c ",
            "JOIN Participate p ON c.classID = p.classID ",
            "JOIN CourseSchedule cs ON c.classID = cs.classID ",
            "WHERE p.traineeID = #{userID} AND cs.dayOfWeek = #{todayDayOfWeek}",
            "</script>"})
    @Results({
            @Result(property = "classID", column = "classID"),
            @Result(property = "courseName", column = "courseName"),
            @Result(property = "courseDescription", column = "courseDescription"),
            @Result(property = "coursePrice", column = "coursePrice"),
            @Result(property = "courseStartTime", column = "courseStartTime"),
            @Result(property = "courseEndTime", column = "courseEndTime"),
            @Result(property = "courseGrade", column = "courseGrade"),
            @Result(property = "coursePhotoUrl", column = "coursePhotoUrl"),
            @Result(property = "classTime", column = "classTime")
    })
    List<CourseInfo> getTodayCoursesByUserID(@Param("userID") int userID,
                                             @Param("todayDayOfWeek") int todayDayOfWeek);


    //教练获取今日课程列表
    @Select({"<script>",
            "SELECT c.classID, c.courseName, c.courseDescription, c.coursePrice, ",
            "c.courseStartTime, c.courseEndTime, c.courseGrade, c.coursePhotoUrl, ",
            "cs.classTime ",
            "FROM Course c ",
            "JOIN Teaches t ON c.classID = t.classID ",
            "JOIN CourseSchedule cs ON c.classID = cs.classID ",
            "WHERE t.coachID = #{userID} AND cs.dayOfWeek = #{todayDayOfWeek}",
            "</script>"})
    @Results({
            @Result(property = "classID", column = "classID"),
            @Result(property = "courseName", column = "courseName"),
            @Result(property = "courseDescription", column = "courseDescription"),
            @Result(property = "coursePrice", column = "coursePrice"),
            @Result(property = "courseStartTime", column = "courseStartTime"),
            @Result(property = "courseEndTime", column = "courseEndTime"),
            @Result(property = "courseGrade", column = "courseGrade"),
            @Result(property = "coursePhotoUrl", column = "coursePhotoUrl"),
            @Result(property = "classTime", column = "classTime")
    })
    List<CourseInfo> getCoachTodayCoursesByUserID(@Param("userID") int userID,
                                                  @Param("todayDayOfWeek") int todayDayOfWeek);


    // 根据查询参数进行搜索
    @SelectProvider(type = CourseSqlBuilder.class, method = "buildSearchCoursesSql")
    @Results({
            @Result(property = "classID", column = "classID"),
            @Result(property = "typeID", column = "typeID"),
            @Result(property = "courseName", column = "courseName"),
            @Result(property = "capacity", column = "Capacity"),
            @Result(property = "courseDescription", column = "courseDescription"),
            @Result(property = "coursePrice", column = "coursePrice"),
            @Result(property = "courseStartTime", column = "courseStartTime"),
            @Result(property = "courseEndTime", column = "courseEndTime"),
            @Result(property = "courseLastDays", column = "courseLastDays"),
            @Result(property = "courseGrade", column = "courseGrade"),
            @Result(property = "coursePhotoUrl", column = "coursePhotoUrl"),
            @Result(property = "courseVideoUrl", column = "courseVideoUrl"),
            @Result(property = "features", column = "features")
    })
    List<Course> searchCourses(CourseSearchParams searchParams);
}
