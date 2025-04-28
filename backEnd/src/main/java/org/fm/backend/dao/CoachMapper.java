package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Coach;

import java.util.List;

@Mapper
public interface CoachMapper {

    @Select("SELECT COUNT(1) FROM Coach WHERE coachID = #{coachID}")
    boolean isIDInCoach(int coachID);

    @Insert("INSERT INTO Coach (coachID, userName, age, gender, iconURL, isMember, coachName, instructorHonors) " +
            "VALUES (#{coachID}, #{userName}, #{age}, #{gender}, #{iconURL}, #{isMember}, #{coachName}, #{instructorHonors})")
    int insert(Coach coach);

    @Select("SELECT * FROM coach WHERE coachID = #{coachID}")
    @Results({
            @Result(property = "coachID", column = "coachID"),
            @Result(property = "userName", column = "userName"),
            @Result(property = "age", column = "age"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "iconURL", column = "iconURL"),
            @Result(property = "isMember", column = "isMember"),
            @Result(property = "coachName", column = "coachName"),
            @Result(property = "instructorHonors", column = "instructorHonors")
    })
    Coach getCoachByCoachID(int coachID);

    @Select("SELECT * FROM coach")
    @Results({
            @Result(property = "coachID", column = "coachID"),
            @Result(property = "userName", column = "userName"),
            @Result(property = "age", column = "age"),
            @Result(property = "gender", column = "gender"),
            @Result(property = "iconURL", column = "iconURL"),
            @Result(property = "isMember", column = "isMember"),
            @Result(property = "coachName", column = "coachName"),
            @Result(property = "instructorHonors", column = "instructorHonors")
    })
    List<Coach> getAllCoaches();
}
