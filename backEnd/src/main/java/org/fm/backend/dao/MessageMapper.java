package org.fm.backend.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.fm.backend.model.Messages;

@Mapper
public interface MessageMapper {
    @Insert("INSERT INTO messages (senderID, receiverID, content, messageType, sendTime) " +
            "VALUES (#{senderID}, #{receiverID}, #{content}, #{messageType}, #{sendTime})")
    @Options(useGeneratedKeys = true, keyProperty = "messageID")
    void insertMessage(Messages message);
}
