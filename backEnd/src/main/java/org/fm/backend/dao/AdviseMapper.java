package org.fm.backend.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Advise;

@Mapper
public interface AdviseMapper
{
    // 插入新的Advise记录
    @Insert("""
        INSERT INTO Advise(classID, coachID, traineeID)
        VALUES(#{classID}, #{coachID}, #{traineeID})
        """)
    boolean insert(Advise advise);

    // 删除指定classID和traineeID的Advise记录
    @Delete("""
        DELETE FROM Advise 
        WHERE classID = #{classID} AND traineeID = #{traineeID}
        """)
    boolean deleteByClassAndTrainee(
            @Param("classID") int classID,
            @Param("traineeID") int traineeID
    );

}
