package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Trainee;

import java.util.List;

@Mapper
public interface TraineeMapper {

    // 根据 classID 获取所有学员
    @Select("""
        SELECT t.traineeID, t.userName, t.Age, t.Gender, t.iconURL, t.traineeName
        FROM Trainee t
        JOIN Participate p ON t.traineeID = p.traineeID
        WHERE p.classID = #{classID}
        """)
    List<Trainee> getAllTraineesByClassID(@Param("classID") int classID);

    // 插入新学员
    @Insert("""
        INSERT INTO Trainee (traineeID, userName, Age, Gender, iconURL, traineeName)
        VALUES (#{traineeID}, #{userName}, #{Age}, #{Gender}, #{iconURL}, #{traineeName})
        """)
    boolean insert(Trainee trainee);

    // 根据 classID 和 traineeID 删除参与记录
    @Delete("""
        DELETE FROM Participate
        WHERE classID = #{classID} AND traineeID = #{traineeID}
        """)
    boolean deleteByClassIDAndTraineeID(
            @Param("classID") int classID,
            @Param("traineeID") int traineeID
    );
}
