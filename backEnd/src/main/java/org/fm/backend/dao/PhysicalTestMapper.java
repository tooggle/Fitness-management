package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.PhysicalTest;

@Mapper
public interface PhysicalTestMapper {

    @Insert("INSERT INTO PhysicalTest (userID, pushups, squats, situps, pullups, long_distance) " +
            "VALUES (#{userID}, NULL, NULL, NULL, NULL, NULL)")
    int initUserPhysicalTest(@Param("userID") int userID);

    @Update("UPDATE PhysicalTest SET " +
            "pushups = #{pushups}, " +
            "squats = #{squats}, " +
            "situps = #{situps}, " +
            "pullups = #{pullups}, " +
            "long_distance = #{longDistance} " +
            "WHERE userID = #{userID}")
    int updatePhysicalTest(
            @Param("userID") int userID,
            @Param("pushups") Integer pushups,
            @Param("squats") Integer squats,
            @Param("situps") Integer situps,
            @Param("pullups") Integer pullups,
            @Param("longDistance") Integer longDistance);

    @Select("SELECT pushups, squats, situps, pullups, long_distance as longDistance " +
            "FROM physical_test WHERE userID = #{userId}")
    PhysicalTest getPhysicalTestData(@Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM physical_test WHERE userID = #{userId}")
    boolean exists(@Param("userId") int userId);
}
