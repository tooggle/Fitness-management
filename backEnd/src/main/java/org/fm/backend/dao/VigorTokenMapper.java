package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.VigorTokenRecord;

import java.util.List;

@Mapper
public interface VigorTokenMapper {
    // 获取活力币余额
    @Select("SELECT vigorTokenBalance FROM User WHERE userID = #{userID}")
    int getVigorTokenBalance(@Param("userID") int userID);

    // 更新活力币余额
    @Update("UPDATE User SET vigorTokenBalance = vigorTokenBalance + #{change} " +
            "WHERE userID = #{userID}")
    boolean updateVigorTokenBalance(@Param("change") int change, @Param("userID") int userID);

    //设置活力币
    @Update("UPDATE User SET vigorTokenBalance = #{vigorTokenBalance} WHERE userID = #{userID}")
    boolean setVigorTokenBalance(int userID,int vigorTokenBalance);

    // 插入活力币变化记录
    @Insert("INSERT INTO Vigortokenrecord (userID, reason, `change`, balance, createTime) " +
            "VALUES (#{userID}, #{reason}, #{change}, #{balance}, #{createTime})")
    @Options(useGeneratedKeys = true, keyProperty = "recordID")
    boolean insertVigorTokenRecord(VigorTokenRecord vigorTokenRecord);

    // 获取用户所有的活力币记录
    @Select("SELECT * FROM Vigortokenrecord WHERE userID = #{userID} ORDER BY createTime DESC")
    List<VigorTokenRecord> getVigorTokenRecordsByUserID(@Param("userID") int userID);
}

