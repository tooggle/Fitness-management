package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FriendshipMapper {

    @Select({
            "SELECT friendID FROM friendship WHERE userID = #{userID}",
            "UNION",
            "SELECT userID FROM friendship WHERE friendID = #{userID}"
    })
    List<Integer> getFriendList(@Param("userID") int userID);

    @Insert("INSERT INTO friendship (userID, friendID) VALUES (#{userID}, #{friendID})")
    @Options(useGeneratedKeys = false)
    boolean addFriend(@Param("userID") int userID, @Param("friendID") int friendID);
}
