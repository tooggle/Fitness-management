package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.feedback;
import org.fm.backend.model.Participate;

import java.util.List;

@Mapper
public interface ParticipateMapper {
    // 插入参与记录
    @Insert("""
        INSERT INTO Participate(traineeID, classID, typeID, Grade, Evaluate)
        VALUES(#{traineeID}, #{classID}, #{typeID}, #{Grade}, #{Evaluate})
        """)
    boolean insert(Participate participate);

    // 删除参与记录
    @Delete("""
        DELETE FROM Participate 
        WHERE classID = #{classID} AND traineeID = #{traineeID}
        """)
    boolean deleteByClassIDAndTraineeID(
            @Param("classID") int classID,
            @Param("traineeID") int traineeID
    );

    // 获取课程评分
    @Select("""
        SELECT Grade 
        FROM Participate 
        WHERE classID = #{classID}
        """)
    List<Integer> getGradesByClassID(@Param("classID") int classID);

    // 查询单个参与记录
    @Select("""
        SELECT * 
        FROM Participate 
        WHERE classID = #{classID} AND traineeID = #{traineeID}
        """)
    Participate getByClassIDAndTraineeID(
            @Param("classID") int classID,
            @Param("traineeID") int traineeID
    );

    // 更新参与记录
    @Update("""
        UPDATE Participate 
        SET typeID = #{typeID}, Grade = #{Grade}, Evaluate = #{Evaluate}
        WHERE classID = #{classID} AND traineeID = #{traineeID}
        """)
    boolean update(Participate participate);

    // 获取课程评论
    @Select("""
        SELECT traineeID, Evaluate as comment, Grade as grade
        FROM Participate 
        WHERE classID = #{classID}
        """)
    List<feedback> getCommentsByClassID(@Param("classID") int classID);

}
