package org.fm.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.fm.backend.model.Achievement;

import java.util.List;

@Mapper
public interface AchievementMapper {
    public static final int _PersonalInfo = 1;       // 完善个人信息
    public static final int _LoginDays = 2;          // 登录天数
    public static final int _CompleteCourse = 3;     // 完成课程
    public static final int _BeLiked = 4;            // 被点赞
    public static final int _BeCommented = 5;        // 被评论
    public static final int _Post = 6;               // 发布内容
    public static final int _CompleteDietPlan = 7;   // 完成饮食计划
    public static final int _CompleteExercisePlan = 8;// 完成锻炼计划
    // 获取所有成就ID
    @Select("SELECT AchievementID FROM Achievement")
    List<Integer> getAllAchievementIds();

    // 获取特定成就的目标值
    @Select("SELECT Target FROM Achievement WHERE AchievementID = #{achievementId}")
    int getAchievementTarget(@Param("achievementId") int achievementId);

    // 获取完整成就信息
    @Select("SELECT * FROM Achievement WHERE AchievementID = #{achievementId}")
    Achievement getAchievementById(@Param("achievementId") int achievementId);

    // 获取所有成就信息
    @Select("SELECT * FROM Achievement")
    List<Achievement> getAllAchievements();
}
