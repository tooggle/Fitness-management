package org.fm.backend.service.ai;

import org.fm.backend.dto.AiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("generalAiService")
public class AiServiceImpl implements AiService {

    @Override
    public String generate(Map<String, Object> params) {
        String exerciseType = (String) params.get("type");
        
        String generatedText = "AI 生成的文本，例如：" + exerciseType;

        AiResponse aiResponse = AiResponse.builder()
                                          .success(true)
                                          .text(generatedText)
                                          .type(exerciseType)
                                          .build();

        return aiResponse.getText();
    }
}
