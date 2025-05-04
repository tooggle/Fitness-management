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

    @Override
    public String analyzeExercise(String base64Img, String exerciseName) {
        String prompt = String.format("""
            请扮演健身教练，根据以下信息给出专业的动作指导和纠正建议：
            - 动作名称：%s
            - 图像（Base64 编码）：%s
            请输出要点清晰、要点分明的文本。
            """, exerciseName, base64Img);

        String aiResult = "AI 分析结果：" + prompt;

        return aiResult;
    }
}
