package org.fm.backend.controller;

import org.fm.backend.dto.AIRes;
import org.fm.backend.util.OpenAIUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AiGuideController {

    private final OpenAIUtil openAIUtil;

    public AiGuideController(OpenAIUtil openAIUtil) {
        this.openAIUtil = openAIUtil;
    }

    // 根据 recordId 生成个性化健身建议
    @GetMapping("/api/ai/guide")
    public AIRes getAiGuide(@RequestParam("recordId") int recordId) {
        String prompt = String.format("请根据健身记录 ID 为 %d 的用户，生成一段简短的个性化健身建议。", recordId);

        try {
            // 调用 OpenAI 接口，返回原始 JSON
            String json = openAIUtil.getAISuggestion(prompt);

            // json 是 {"choices":[{"message":{"content":"建议内容"}}], ...}
            String content = extractContent(json);

            return AIRes.builder()
                        .suggestion(content)
                        .build();

        } catch (Exception e) {
            return AIRes.builder()
                        .suggestion("AI 服务调用失败：" + e.getMessage())
                        .build();
        }
    }

    private String extractContent(String rawJson) {
        // 极简字符串查找，拿到第一个 "content":"...”
        int idx = rawJson.indexOf("\"content\"");
        if (idx == -1) return "没有获取到 AI 建议内容";
        int start = rawJson.indexOf("\"", idx + 10) + 1;
        int end = rawJson.indexOf("\"", start);
        if (start < 0 || end < 0) return "解析 AI 返回内容出错";
        String msg = rawJson.substring(start, end)
                            .replace("\\n", "\n")
                            .replace("\\\"", "\"");
        return msg;
    }
}
