package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Teaches;

@Mapper
public interface TeachesMapper {

    // 根据 classID 查询单条记录
    @Select("""
        SELECT coachID, classID, typeID 
        FROM Teaches 
        WHERE classID = #{classID}
        """)
    Teaches getByClassID(@Param("classID") int classID);

    // 插入新记录
    @Insert("""
        INSERT INTO Teaches (coachID, classID, typeID)
        VALUES (#{coachID}, #{classID}, #{typeID})
        """)
    boolean insert(Teaches teaches);

    // 根据 coachID 和 classID 删除记录（支持事务）
    @Delete("""
        DELETE FROM Teaches 
        WHERE coachID = #{coachID} AND classID = #{classID}
        """)
    boolean delete(@Param("coachID") int coachID, @Param("classID") int classID);
}
