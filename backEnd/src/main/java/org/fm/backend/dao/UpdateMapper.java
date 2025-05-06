package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Update;

import java.util.List;

@Mapper
public interface UpdateMapper {
    @Insert("INSERT INTO `Update` (coachID, classID, actionType, updateTime, updateContext) " +
            "VALUES (#{coachID}, #{classID}, #{actionType}, #{updateTime}, #{updateContext})")
    int insert(Update update);

    @Select("SELECT * FROM `Update`")
    @Results({
            @Result(property = "coachID", column = "coachID"),
            @Result(property = "classID", column = "classID"),
            @Result(property = "actionType", column = "actionType"),
            @Result(property = "updateTime", column = "updateTime"),
            @Result(property = "updateContext", column = "updateContext")
    })
    List<Update> findAll();
}
