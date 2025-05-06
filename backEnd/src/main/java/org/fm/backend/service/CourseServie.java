package org.fm.backend.service;

import org.fm.backend.dao.*;
import org.fm.backend.dto.*;
import org.fm.backend.model.*;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
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

    @Autowired
    UserMapper userMapper;

    @Autowired
    TraineeMapper traineeMapper;

    @Autowired
    BookMapper bookMapper;

    @Autowired
    ParticipateMapper participateMapper;

    @Autowired
    AdviseMapper adviseMapper;

    @Autowired
    VigorTokenService vigorTokenService;

    @Autowired
    PaymentMapper paymentMapper;

    public String publishCourse(String token, Course course, List<CourseSchedule> courseSchedules) {
        TokenValidationResult result = jwtHelper.validateToken(token);
        if (!Objects.equals(result.Role, "coach")) {
            return "身份权限不符！";
        }
        if (!courseMapper.insert(course)) {
            return "课程发布失败：无法插入课程信息";
        }
        int classID = course.getClassID();
        Teaches teaches = new Teaches();
        teaches.setCoachID(result.userID);
        teaches.setClassID(classID);
        teaches.setTypeID(course.getTypeID());
        if (!teachesMapper.insert(teaches)) {
            return "课程发布失败：无法插入教练授课信息";
        }
        for (CourseSchedule schedule : courseSchedules) {
            if (!courseScheduleMapper.insertCourseSchedule(classID, schedule.getDayOfWeek(), schedule.getClassTime())) {
                return "课程发布失败: 无法插入授课时间信息";
            }
        }
        return "课程发布成功";
    }

    public String modifyCourse(String token, Course course, List<CourseSchedule> courseSchedules) {
        TokenValidationResult result = jwtHelper.validateToken(token);
        if (!Objects.equals(result.Role, "coach")) {
            return "身份权限不符！";
        }
        if (!courseMapper.updateByClassID(course)) {
            return "课程修改失败";
        }
        if (!courseScheduleMapper.deleteCourseSchedulesByClassID(course.getClassID())) {
            return "删除课程时间表失败";
        }
        for (CourseSchedule schedule : courseSchedules) {
            if (!courseScheduleMapper.insertCourseSchedule(course.getClassID(), schedule.getDayOfWeek(), schedule.getClassTime())) {
                return "插入课程时间表失败";
            }
        }
        return "课程修改成功";
    }

    public String deleteCourseByClassID(String token, int classID){
        TokenValidationResult result = jwtHelper.validateToken(token);
        if(!courseMapper.deleteByClassID(classID)){
            return "课程删除失败：无法删除课程信息";
        }
        teachesMapper.delete(result.userID,classID);
        courseScheduleMapper.deleteCourseSchedulesByClassID(classID);
        return "课程删除成功";
    }

    public Course getCourseByClassID(String token,int classID){
        return courseMapper.getCourseByClassID(classID);
    }

    public List<CourseDTO> getAllCourse(String token){
        TokenValidationResult result = jwtHelper.validateToken(token);
        List<CourseDTO> ans = new ArrayList<>();
        List<Course> courses = courseMapper.getAllCourseRandomly();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Course course : courses){
            int userID = teachesMapper.getByClassID(course.getClassID()).getCoachID();
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.coursePhotoUrl = course.getCoursePhotoUrl();
            courseDTO.courseName = course.getCourseName();
            courseDTO.courseDescription = course.getCourseDescription();
            courseDTO.courseStartTime = sdf.format(course.getCourseStartTime());
            courseDTO.courseEndTime = sdf.format(course.getCourseEndTime());
            courseDTO.courseGrade = String.valueOf(course.getCourseGrade());
            courseDTO.coursePrice = course.getCoursePrice();
            courseDTO.coachName = userMapper.getUserByUserId(userID).getUserName();
            courseDTO.instructorHonors = userMapper.getUserByUserId(userID).getIntroduction();
            courseDTO.iconURL = userMapper.getUserByUserId(userID).getIconURL();
            courseDTO.features = course.getFeatures();
            courseDTO.courseType = courseMapper.getTypeNameByTypeID(course.getTypeID());
            courseDTO.capacity = course.getCapacity();
            courseDTO.isBooked = 0;
            courseDTO.schedules = courseScheduleMapper.getCourseSchedulesByClassID(course.getClassID());
            ans.add(courseDTO);
        }
        return ans;
    }

    public List<Trainee> getAllTraineesByClassID(String token ,int classID){
        return traineeMapper.getAllTraineesByClassID(classID);
    }

    public BookDTO reserveCourse(String token, int[] classIDs, String payMethod) {
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        Date now = new Date();
        List<Integer> bookIDs = new ArrayList<>();
        String message = "课程预订成功";
        for (int classID : classIDs) {
            Book book = new Book();
            book.setClassID(classID);
            book.setTraineeID(tokenRes.userID);
            book.setPayMethod(payMethod);
            book.setBookStatus(1); // 1表示已预订状态
            book.setBookTime(now);
            book.setPaymentID(-1); // 初始支付ID
            bookMapper.insert(book);
            bookIDs.add(book.getBookID());
        }
        return new BookDTO(
                bookIDs.stream().mapToInt(i -> i).toArray(),
                message
        );
    }

    public String payCourseFare(String token, int[] bookIDs,String payMethod){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int traineeID = tokenRes.userID;
        for (int bookID : bookIDs){
            Course course = courseMapper.getCourseByClassID(bookMapper.getBookByID(bookID).getClassID());
            int amount  =course.getCoursePrice();
            String courseName = course.getCourseName();
            int fare = -amount;
            String message = "购买课程:" + courseName + ", 耗费 " + amount + " 活力币";
            vigorTokenService.updateBalance(traineeID,message,fare);
            Payment payment = new Payment();
            payment.setAmount(amount);
            payment.setPayMethod(payMethod);
            payment.setBookID(bookID);
            payment.setPayTime(new Date());
            paymentMapper.insert(payment);
            int payID = payment.getPaymentID();
            bookMapper.updateBookStatusAndPaymentID(bookID,2,payID);
            Advise advise = new Advise();
            advise.setClassID(bookMapper.getBookByID(bookID).getClassID());
            advise.setCoachID(teachesMapper.getByClassID(bookMapper.getBookByID(bookID).getClassID()).getCoachID());
            advise.setTraineeID(traineeID);
            adviseMapper.insert(advise);
            Participate participate = new Participate();
            participate.setClassID(bookMapper.getBookByID(bookID).getClassID());
            participate.setTraineeID(traineeID);
            participate.setTypeID(courseMapper.getCourseByClassID(bookMapper.getBookByID(bookID).getClassID()).getTypeID());
            participateMapper.insert(participate);
        }
        return "支付课程费用成功!";
    }

    public List<courseDetail> getReservedCourseByUserID(String token) {
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int userID = tokenRes.userID;
        List<courseDetail> courseDetails = new ArrayList<>();
        List<BookCourseInfo> courseInfos = courseMapper.getReservedCourseByUserID(userID);
        for(BookCourseInfo courseInfo : courseInfos){
            courseDetail cc = new courseDetail();
            cc.coursePhotoUrl = courseInfo.getCoursePhotoUrl();
            cc.courseName = courseInfo.getCourseName();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            cc.courseStartTime = sdf.format(courseInfo.getCourseStartTime());
            cc.courseEndTime = sdf.format(courseInfo.getCourseEndTime());
            cc.coursePrice = courseInfo.getCoursePrice();
            cc.classID = courseInfo.getClassID();
            cc.bookID = courseInfo.getBookID();
            cc.schedules = courseScheduleMapper.getCourseSchedulesByClassID(courseInfo.getClassID());
            courseDetails.add(cc);
        }
        return courseDetails;
    }

    public List<CourseDTO> getParticipatedCourseByUserID(String token){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int userID = tokenRes.userID;
        List<CourseDTO> courseDTOs = new ArrayList<>();
        List<Course> courses = courseMapper.getParticipatedCourseByUserID(userID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Course course : courses){
            String typeName = courseMapper.getTypeNameByTypeID(course.getTypeID());
            int coachID = courseMapper.getCoachIDByClassID(course.getClassID());
            User user = userMapper.getUserByUserId(coachID);
            String coachName = user.getUserName();
            String instructorHonors = user.getIntroduction();
            String iconURL = user.getIconURL();
            List<CourseSchedule> schedules = courseScheduleMapper.getCourseSchedulesByClassID(course.getClassID());
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.coursePhotoUrl = course.getCoursePhotoUrl();
            courseDTO.courseName = course.getCourseName();
            courseDTO.courseDescription = course.getCourseDescription();
            courseDTO.courseStartTime = sdf.format(course.getCourseStartTime());
            courseDTO.courseEndTime = sdf.format(course.getCourseEndTime());
            courseDTO.coursePrice = course.getCoursePrice();
            courseDTO.courseGrade = String.valueOf(course.getCourseGrade());
            courseDTO.coachName = coachName;
            courseDTO.capacity = course.getCapacity();
            courseDTO.instructorHonors = instructorHonors;
            courseDTO.iconURL = iconURL;
            courseDTO.features = course.getFeatures();
            courseDTO.courseType = typeName;
            courseDTO.schedules = schedules;
            courseDTOs.add(courseDTO);
        }
        return courseDTOs;
    }

    public List<CourseDTO> getPublishedCourseByUserID(String token) {
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int userID = tokenRes.userID;
        List<CourseDTO> courseDTOs = new ArrayList<>();
        List<Course> courses = courseMapper.getPublishedCourseByUserID(userID);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        for(Course course : courses){
            User user = userMapper.getUserByUserId(userID);
            CourseDTO courseDTO = new CourseDTO();
            courseDTO.coursePhotoUrl = course.getCoursePhotoUrl();
            courseDTO.courseName = course.getCourseName();
            courseDTO.courseDescription = course.getCourseDescription();
            courseDTO.courseStartTime = sdf.format(course.getCourseStartTime());
            courseDTO.courseEndTime = sdf.format(course.getCourseEndTime());
            courseDTO.coursePrice = course.getCoursePrice();
            courseDTO.courseGrade = String.valueOf(course.getCourseGrade());
            courseDTO.courseName = user.getUserName();
            courseDTO.capacity = course.getCapacity();
            courseDTO.instructorHonors = user.getIntroduction();
            courseDTO.iconURL = user.getIconURL();
            courseDTO.features = course.getFeatures();
            courseDTO.courseType =courseMapper.getTypeNameByTypeID(course.getTypeID());
            courseDTO.schedules = courseScheduleMapper.getCourseSchedulesByClassID(course.getClassID());
            courseDTOs.add(courseDTO);
        }
        return courseDTOs;
    }


    public List<CourseInfo> getTodayCoursesByUserID(String token) {
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int userID = tokenRes.userID;
        return courseMapper.getTodayCoursesByUserID(userID, LocalDate.now().getDayOfWeek().getValue() % 7);
    }

    public List<CourseInfo> getCoachTodayCoursesByUserID(String token) {
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int userID = tokenRes.userID;
        return courseMapper.getCoachTodayCoursesByUserID(userID, LocalDate.now().getDayOfWeek().getValue() % 7);
    }

    public List<Course> searchCourse(String token, String keyword, Integer typeID, Integer minPrice, Integer maxPrice) {
        CourseSearchParams params = new CourseSearchParams();
        params.setCourseName(keyword);
        params.setTypeID(typeID);
        params.setMinPrice(minPrice);
        params.setMaxPrice(maxPrice);
        return courseMapper.searchCourses(params);
    }

    public String gradeCourse(String token,int classID, int grade){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int traineeID = tokenRes.userID;
        Participate participate = participateMapper.getByClassIDAndTraineeID(classID, traineeID);
        if(participate == null){
            return "未找到该课程的参与记录";
        }
        participate.setGrade(grade);
        participateMapper.update(participate);
        return "评分成功";
    }

    public String publishComment(String token,int classID, String comment){
        TokenValidationResult tokenRes = jwtHelper.validateToken(token);
        int traineeID = tokenRes.userID;
        Participate participate = participateMapper.getByClassIDAndTraineeID(classID, traineeID);
        if(participate == null){
            return "未找到该课程的参与记录";
        }
        participate.setEvaluate(comment);
        participateMapper.update(participate);
        return "评论发布成功";
    }
}

