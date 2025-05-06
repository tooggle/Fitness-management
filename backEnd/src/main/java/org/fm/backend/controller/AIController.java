package org.fm.backend.controller;

import jakarta.annotation.Resource;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {

    @Resource
    private OpenAiChatModel chatModel;

    @GetMapping("/ask")
    public String ask(@RequestParam String question) {
        return chatModel.call(question);
    }
}
