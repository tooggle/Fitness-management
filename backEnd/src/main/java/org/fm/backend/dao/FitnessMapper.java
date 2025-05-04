package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.model.Fitness;

@Mapper
public interface FitnessMapper {

    @Insert("INSERT INTO Fitness (userID, height, weight, BMI, bodyFatRate) " +
            "VALUES (#{userID}, NULL, NULL, NULL, NULL)")
    int initUserFitnessData(@Param("userID") int userID);

    @Update("UPDATE Fitness SET " +
            "height = #{height}, " +
            "weight = #{weight}, " +
            "BMI = #{BMI}, " +
            "bodyFatRate = #{bodyFatRate} " +
            "WHERE userID = #{userID}")
    int updateFitnessData(
            @Param("userID") int userID,
            @Param("height") Double height,
            @Param("weight") Double weight,
            @Param("BMI") Double BMI,
            @Param("bodyFatRate") Double bodyFatRate);

    @Select("SELECT height, weight, BMI, bodyFatRate FROM fitness WHERE userID = #{userId}")
    Fitness getFitnessData(@Param("userId") int userId);

    @Select("SELECT COUNT(*) FROM Fitness WHERE userID = #{userId}")
    boolean exists(@Param("userId") int userId);

}
