package org.fm.backend.service;

import org.fm.backend.util.OpenAIUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class FitnessAIGuideService {

    private final OpenAIUtil openAIUtil;

    public FitnessAIGuideService(OpenAIUtil openAIUtil) {
        this.openAIUtil = openAIUtil;
    }

    /**
     * 根据健身记录参数生成训练建议。
     *
     * @param userId 用户ID
     * @param date  记录日期
     * @param exerciseType 运动类型
     * @param duration 时长（分钟）
     * @param caloriesBurned 消耗卡路里（大卡）
     * @param notes 额外备注，null 或空时视为 "无"
     * @return AI 生成的训练建议
     */
    @Transactional(readOnly = true)
    public String generateAdvice(Long userId,
                                 LocalDate date,
                                 String exerciseType,
                                 int duration,
                                 int caloriesBurned,
                                 String notes) {
        String prompt = buildPrompt(userId, date, exerciseType, duration, caloriesBurned, notes);
        return openAIUtil.getAISuggestion(prompt);
    }

    private String buildPrompt(Long userId,
                               LocalDate date,
                               String exerciseType,
                               int duration,
                               int caloriesBurned,
                               String notes) {
        StringBuilder sb = new StringBuilder();
        sb.append("请根据以下健身记录给出专业的训练建议：\n");
        sb.append("用户ID: ").append(userId).append("\n");
        sb.append("日期: ").append(date).append("\n");
        sb.append("运动类型: ").append(exerciseType).append("\n");
        sb.append("时长: ").append(duration).append(" 分钟\n");
        sb.append("消耗卡路里: ").append(caloriesBurned).append(" 大卡\n");
        sb.append("额外备注: ").append((notes == null || notes.isEmpty()) ? "无" : notes).append("\n");
        sb.append("请给出3点改进建议，并简要说明理由。");
        return sb.toString();
    }
}
