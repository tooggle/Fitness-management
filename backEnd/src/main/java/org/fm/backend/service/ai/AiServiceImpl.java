package org.fm.backend.service.ai;

import org.fm.backend.dto.AiResponse;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service("generalAiService")
public class AiServiceImpl implements AiService {

    @Override
    public String generate(Map<String, Object> params) {
        // 假设从 params 中获取 type 和其他相关参数
        String exerciseType = (String) params.get("type"); // 从 params 获取运动类型（示例）
        
        // AI 生成的文本，假设你有一些逻辑来生成这个文本
        String generatedText = "AI 生成的文本，例如：" + exerciseType;

        // 创建 AiResponse 对象（如果需要）
        AiResponse aiResponse = AiResponse.builder()
                                          .success(true)  // 假设返回成功
                                          .text(generatedText)  // 返回生成的文本
                                          .type(exerciseType)  // 返回运动类型
                                          .build();

        // 返回生成的文本部分，作为字符串
        return aiResponse.getText();  // 只返回文本部分，作为字符串
    }
}
