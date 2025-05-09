package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.UserAchievement;

import java.util.List;

@Mapper
public interface UserAchievementMapper {
    // 初始化用户成就记录
    @Insert("INSERT INTO UserAchievement (userID, achievementID, progress, achievedDate, isAchieved) " +
            "VALUES (#{userId}, #{achievementId}, 0, NULL, 0)")
    int initUserAchievement(@Param("userId") int userId, @Param("achievementId") int achievementId);

    // 获取用户成就进度
    @Select("SELECT progress FROM UserAchievement " +
            "WHERE userID = #{userId} AND achievementID = #{achievementId}")
    int getProgress(
            @Param("userId") int userId,
            @Param("achievementId") int achievementId);

    // 检查是否已完成成就
    @Select("SELECT isAchieved FROM UserAchievement " +
            "WHERE userID = #{userId} AND achievementID = #{achievementId}")
    Boolean isAchieved(
            @Param("userId") int userId,
            @Param("achievementId") int achievementId);

    // 获取成就的目标值
    @Select("SELECT target FROM Achievement " +
            "WHERE achievementID = #{achievementId}")
    int getAchievementTarget(@Param("achievementId") int achievementId);

    // 更新用户的成就进度和达成状态
    @Update("UPDATE UserAchievement SET progress = #{progress}, isAchieved = #{isAchieved}, " +
            "achievedDate = CASE WHEN #{isAchieved} = TRUE THEN NOW() ELSE achievedDate END " +
            "WHERE userID = #{userId} AND achievementID = #{achievementId}")
    void update(
            @Param("userId") int userId,
            @Param("achievementId") int achievementId,
            @Param("progress") int progress,
            @Param("isAchieved") boolean isAchieved);

}

