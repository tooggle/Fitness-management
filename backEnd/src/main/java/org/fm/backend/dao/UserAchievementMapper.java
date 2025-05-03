package org.fm.backend.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
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
    boolean isAchieved(
            @Param("userId") int userId,
            @Param("achievementId") int achievementId);


}

