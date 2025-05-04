package org.fm.backend.controller;

import org.fm.backend.dto.AIRes;
import org.fm.backend.util.OpenAIUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiGuideController {

    private final FitnessAIGuideService guideService;

    public AiGuideController(FitnessAIGuideService guideService) {
        this.guideService = guideService;
    }

    @GetMapping("/guide")
    public AIRes getAiGuide(@RequestParam("recordId") Long recordId) {
        try {
            String advice = guideService.generateAdvice(recordId);
            return AIRes.builder()
                        .suggestion(advice)
                        .build();
        } catch (IllegalArgumentException e) {
            return AIRes.builder()
                        .suggestion("无效的记录 ID：" + e.getMessage())
                        .build();
        } catch (Exception e) {
            return AIRes.builder()
                        .suggestion("AI 服务调用失败：" + e.getMessage())
                        .build();
        }
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
