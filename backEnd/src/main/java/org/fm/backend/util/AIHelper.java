package org.fm.backend.util;

import jakarta.annotation.Resource;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Component;

@Component
public class AIHelper {
    @Resource
    private OpenAiChatModel chatModel;

    public String getResponse(String question){
        return chatModel.call(question);
    }
}
