package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.MessageMapper;
import org.fm.backend.model.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Slf4j
public class MessageService{

    @Autowired
    private MessageMapper messageMapper;

    public void sendMessage(int senderId, int receiverId, String content, String messageType) {
        // 创建消息对象
        Messages message = new Messages();
        message.setSenderID(senderId);
        message.setReceiverID(receiverId);
        message.setContent(content);
        message.setMessageType(messageType);
        // 将LocalDateTime转换为Date
        message.setSendTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));


        // 保存消息到数据库
        messageMapper.insertMessage(message);

        log.info("发送message给,{}", receiverId);
    }
}
