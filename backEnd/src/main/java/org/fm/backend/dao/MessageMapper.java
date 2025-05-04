package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.Messages;

import java.util.List;

@Mapper
public interface MessageMapper {

    @Insert("INSERT INTO Messages (senderID, receiverID, content, messageType, sendTime,isRead) " +
            "VALUES (#{senderID}, #{receiverID}, #{content}, #{messageType}, #{sendTime}, #{isRead})")
    @Options(useGeneratedKeys = true, keyProperty = "messageID")
    boolean insertMessages(Messages message);


    @Select("SELECT * FROM Messages WHERE receiverID = #{userId} ORDER BY sendTime DESC")
    List<Messages> getMessages(@Param("userId") int userId);

    @Select("SELECT * FROM Messages WHERE receiverID = #{userId} union SELECT * FROM Messages WHERE senderID = #{userId}")
    List<Messages> getMessagesByUserID(int userId);

    @Update("UPDATE Messages SET isRead = 1 WHERE messageID = #{messageId}")
    int markMessagesAsRead(@Param("messageId") int messageId);

}
