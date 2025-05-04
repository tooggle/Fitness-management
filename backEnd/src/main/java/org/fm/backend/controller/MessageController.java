package org.fm.backend.controller;

import org.fm.backend.model.Messages;
import org.fm.backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Message")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping("/GetMessages")
    public List<Messages> getMessages(String token) {
        return messageService.getMessages(token);
    }

    @GetMapping("/MarkedMessagesAsRead")
    public String markMessagesAsRead(int messageId) {
        messageService.markMessagesAsRead(messageId);
        return "标记成功";
    }

    @GetMapping("/GetChatHistory")
    public List<Messages> getChatHistory(int userID) {
        return messageService.getChatHistory(userID);
    }
}
