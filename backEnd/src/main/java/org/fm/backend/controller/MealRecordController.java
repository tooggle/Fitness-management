package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.*;
import org.fm.backend.util.JWTHelper;
import org.fm.backend.service.MealRecordService;
import org.fm.backend.service.ai.AiService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
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
    private AiService generalAiService;

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

    @GetMapping("/AISuggestions")
    public AIRes aISuggestions(@RequestParam int recordID) {
        Map<String, Object> params = new HashMap<>();
        params.put("recordID", recordID);
        AiRequest req = AiRequest.builder()
                                 .type("meal")
                                 .params(params)
                                 .build();
        String suggestion = generalAiService.generate(req.toMap());
        return AIRes.builder()
                    .success(true)
                    .suggestion(suggestion)
                    .build();
    }

    @GetMapping("/GetAISummary")
    public ResultMessage getAISummary(@RequestParam String token,
                                      @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        int userId = jwtHelper.validateToken(token).getUserID();

        
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("date", date.toString());

        AiRequest req = AiRequest.builder()
                                 .type("meal_summary")
                                 .params(params)
                                 .build();

        String summary = generalAiService.generate(req.toMap());

        return ResultMessage.builder()
                            .code(0)
                            .message("success")
                            .data(summary)
                            .build();
    }

    public static class AIResBuilder {
        private String suggestion;
        private boolean success;

        public AIResBuilder suggestion(String suggestion) {
            this.suggestion = suggestion;
            return this;
        }

        public AIResBuilder success(boolean success) {
            this.success = success;
            return this;
        }

        public AIRes build() {
            return new AIRes(success, suggestion);
        }
    }

    public static class AIRes {
        private final boolean success;
        private final String suggestion;

        public AIRes(boolean success, String suggestion) {
            this.success = success;
            this.suggestion = suggestion;
        }

        public static AIResBuilder builder() {
            return new AIResBuilder();
        }
    }
}
