package org.fm.backend.service;

import org.fm.backend.entity.FitnessRecord;
import org.fm.backend.repository.FitnessRecordRepository;
import org.fm.backend.util.OpenAIUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FitnessAIGuideService {

    private final FitnessRecordRepository recordRepository;
    private final OpenAIUtil openAIUtil;

    public FitnessAIGuideService(FitnessRecordRepository recordRepository,
                                 OpenAIUtil openAIUtil) {
        this.recordRepository = recordRepository;
        this.openAIUtil = openAIUtil;
    }

    @Transactional(readOnly = true)
    public String generateAdvice(Long recordId) {
        FitnessRecord rec = recordRepository.findById(recordId)
            .orElseThrow(() -> new IllegalArgumentException("Record not found for id: " + recordId));

        String prompt = buildPromptFromRecord(rec);

        String aiResponse = openAIUtil.getAISuggestion(prompt);

        return aiResponse;
    }

    private String buildPromptFromRecord(FitnessRecord rec) {
        StringBuilder sb = new StringBuilder();
        sb.append("请根据以下健身记录给出专业的训练建议：\n");
        sb.append("用户ID: ").append(rec.getUserId()).append("\n");
        sb.append("日期: ").append(rec.getDate()).append("\n");
        sb.append("运动类型: ").append(rec.getExerciseType()).append("\n");
        sb.append("时长: ").append(rec.getDuration()).append(" 分钟\n");
        sb.append("消耗卡路里: ").append(rec.getCaloriesBurned()).append(" 大卡\n");
        sb.append("额外备注: ").append(
            rec.getNotes() == null ? "无" : rec.getNotes()
        ).append("\n");
        sb.append("请给出3点改进建议，并简要说明理由。");
        return sb.toString();
    }
}
