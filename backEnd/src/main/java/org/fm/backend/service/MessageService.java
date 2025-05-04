package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.MessageMapper;
import org.fm.backend.dto.TokenValidationResult;
import org.fm.backend.model.Messages;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MessageService{

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private JWTHelper jwtHelper;

    public void sendMessage(int senderId, int receiverId, String content, String messageType) {
        // 创建消息对象
        Messages message = new Messages();
        message.setSenderID(senderId);
        message.setReceiverID(receiverId);
        message.setContent(content);
        message.setMessageType(messageType);
        message.setSendTime(new Date());
        message.setIsRead(0);
        // 保存消息到数据库
        messageMapper.insertMessages(message);

        log.info("发送message给,{}", receiverId);
    }

    public List<Messages> getMessages(String token) {
        TokenValidationResult result = jwtHelper.validateToken(token);
        int userID = result.userID;
        return messageMapper.getMessages(userID);
    }

    public void markMessagesAsRead(int messageId) {
        messageMapper.markMessagesAsRead(messageId);
    }

    public List<Messages> getChatHistory(int userID) {
        return messageMapper.getMessagesByUserID(userID);
    }
}
