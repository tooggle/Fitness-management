package org.fm.backend.controller;

import org.fm.backend.dto.AIRes;
import org.fm.backend.service.FitnessAIGuideService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * 从原始 JSON 中提取 content 字段的值
     * @param rawJson AI 原始返回的 JSON 字符串
     * @return 提取后的内容，若出错则返回提示信息
     */
    private String extractContent(String rawJson) {
        int idx = rawJson.indexOf("\"content\"");
        if (idx == -1) {
            return "没有获取到 AI 建议内容";
        }
        int start = rawJson.indexOf("\"", idx + 10) + 1;
        if (start < 1) {
            return "解析 AI 返回内容出错";
        }
        int end = rawJson.indexOf("\"", start);
        if (end < 0) {
            return "解析 AI 返回内容出错";
        }
        String msg = rawJson.substring(start, end)
                            .replace("\\n", "\n")
                            .replace("\\\"", "\"");
        return msg;
    }
}
