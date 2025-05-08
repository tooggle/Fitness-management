package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.*;
import org.fm.backend.util.AIHelper;
import org.fm.backend.util.JWTHelper;
import org.fm.backend.service.MealRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Slf4j
@RestController
@RequestMapping("/api/MealRecords")
public class MealRecordController {
    @Autowired
    private MealRecordService mealRecordService;
    @Autowired
    private JWTHelper jwtHelper;
    @Autowired
    AIHelper aiHelper;

//    @Autowired
//    private AiService generalAiService;

    @PostMapping("/Create")
    public MealRecordRes createMealRecord(@RequestParam String token, @RequestBody CreateMealRecord createMealRecord) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            throw new RuntimeException("Invalid token");
        }
        int userID = validationResult.getUserID();
        System.out.println("创建一条饮食记录, userID: " + userID);

        createMealRecord.setMealTime(createMealRecord.getMealTime());

        return mealRecordService.createMealRecord(userID, createMealRecord);
    }

    @GetMapping("/GetAllDetails")
    public GetAllMealRecordsDetailsRes getAllDetails(
            @RequestParam String token,
            @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        int userID = jwtHelper.validateToken(token).getUserID();

        return mealRecordService.getAllDetails(userID, date);
    }

    @PutMapping("/Update")
    public ResultMessage updateMealRecords(@RequestBody UpdateMealRecordInfo updateMealRecordInfo) {
        log.info("更新饮食记录：{}",updateMealRecordInfo);
        return mealRecordService.updateMealRecords(updateMealRecordInfo);
    }

    @DeleteMapping("/Delete")
    public ResultMessage deleteMealRecord(@RequestParam int recordID) {
        return mealRecordService.deleteMealRecord(recordID);
    }

//    @GetMapping("/AISuggestions")
//    public AIRes aISuggestions(@RequestParam int recordID) {
//        Map<String, Object> params = new HashMap<>();
//        params.put("recordID", recordID);
//        AiRequest req = AiRequest.builder()
//                                 .type("meal")
//                                 .params(params)
//                                 .build();
//        String suggestion = generalAiService.generate(req.toMap());
//        return AIRes.builder()
//                    .success(true)
//                    .suggestion(suggestion)
//                    .build();
//    }
//
    @GetMapping("/GetAISummary")
    public AISum getAISummary(@RequestParam String token,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            throw new RuntimeException("Invalid token");
        }
        int userId = jwtHelper.validateToken(token).getUserID();

        List<AISummaryRes> summaries = mealRecordService.getAISummary(userId, date);

        log.info("正在生成AI总结，{}",summaries);

        // 2. 构建 prePrompt
        StringBuilder prePrompt = new StringBuilder();
        prePrompt.append("- Role: 健身教练\n");
        prePrompt.append("- Background: 用户希望通过获得专业的饮食分析与调整建议。\n");
        prePrompt.append("- Profile: 你是一位经验丰富的健身教练，拥有专业的营养学知识和丰富的实践经验。\n");
        prePrompt.append("- Skills: 营养学、饮食分析、健康指导、个性化饮食建议。\n");
        prePrompt.append("- Goals: 提供准确的饮食分析，确保用户的饮食健康，给出针对性的改进建议。\n");
        prePrompt.append("- Constrains: 分析应基于科学依据，建议应易于用户理解和执行;字数不要太多，表述要精简\n");
        prePrompt.append("- OutputFormat: 不要有任何多余的问候语，直接给出饮食分析结果和具体调整建议！\n");
        prePrompt.append("- Workflow:\n");
        prePrompt.append("1. 分析用户的饮食记录，识别食物种类和摄入量。\n");
        prePrompt.append("2. 根据分析结果，给出饮食调整的具体建议。\n");

        // 添加具体的饮食记录信息
        prePrompt.append("用户的饮食记录如下：\n");
        for (AISummaryRes summary : summaries) {
            prePrompt.append("时间: ").append(summary.getMealTime()).append("\n");
            prePrompt.append("食物: \n");
            for (AISummaryRes.FoodRecord food : summary.getFoods()) {
                prePrompt.append("- ").append(food.getFoodName()).append(": ").append(food.getFoodAmount()).append("克\n");
            }
            prePrompt.append("\n");
        }

        // 3. 调用 AIHelper 获取响应
        String ans = aiHelper.getResponse(prePrompt.toString());

        // 4. 返回结果
        return AISum.builder().message(ans).build();
    }
//
//    public static class AIResBuilder {
//        private String suggestion;
//        private boolean success;
//
//        public AIResBuilder suggestion(String suggestion) {
//            this.suggestion = suggestion;
//            return this;
//        }
//
//        public AIResBuilder success(boolean success) {
//            this.success = success;
//            return this;
//        }
//
//        public AIRes build() {
//            return new AIRes(success, suggestion);
//        }
//    }
//
//    public static class AIRes {
//        private final boolean success;
//        private final String suggestion;
//
//        public AIRes(boolean success, String suggestion) {
//            this.success = success;
//            this.suggestion = suggestion;
//        }
//
//        public static AIResBuilder builder() {
//            return new AIResBuilder();
//        }
//    }
}
