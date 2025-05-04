package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.*;
import org.fm.backend.util.JWTHelper;
import org.fm.backend.service.MealRecordService;
import org.fm.backend.service.impl.MealAIGuideService;
import org.fm.backend.service.impl.AISummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/api/meal-records")
public class MealRecordController {

    private final MealRecordService mealRecordService;
    private final JWTHelper jwtHelper;
    private final MealAIGuideService mealAIGuideService;
    private final AISummaryService aiSummaryService;

    public MealRecordController(MealRecordService mealRecordService,
                                JWTHelper jwtHelper,
                                MealAIGuideService mealAIGuideService,
                                AISummaryService aiSummaryService) {
        this.mealRecordService = mealRecordService;
        this.jwtHelper = jwtHelper;
        this.mealAIGuideService = mealAIGuideService;
        this.aiSummaryService = aiSummaryService;
    }

    @PostMapping
    public MealRecordRes create(@RequestParam String token,
                                @RequestBody CreateMealRecord dto) {
        TokenValidationResult v = jwtHelper.validateToken(token);
        if (!v.isValid()) {
            throw new UnauthorizedException("Invalid token");
        }
        log.info("Creating meal record for userId={}", v.getUserID());
        return mealRecordService.createMealRecord(v.getUserID(), dto);
    }

    @GetMapping("/details")
    public GetAllMealRecordsDetailsRes list(
            @RequestParam String token,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        TokenValidationResult v = jwtHelper.validateToken(token);
        if (!v.isValid()) {
            throw new UnauthorizedException("Invalid token");
        }
        return mealRecordService.getAllDetails(v.getUserID(), date);
    }

    @PutMapping
    public ResultMessage update(@RequestBody UpdateMealRecordInfo dto) {
        log.info("Updating meal records: {}", dto);
        return mealRecordService.updateMealRecords(dto);
    }

    @DeleteMapping("/{id}")
    public ResultMessage delete(@PathVariable("id") int recordId) {
        return mealRecordService.deleteMealRecord(recordId);
    }

    @GetMapping("/{id}/ai-suggestions")
    public AIRes aiSuggestions(@PathVariable("id") Long recordId) {
        String suggestion = mealAIGuideService.generateMealAdvice(recordId);
        return AIRes.builder()
                    .suggestion(suggestion)
                    .build();
    }

    @GetMapping("/ai-summary")
    public ResultMessage aiSummary(
            @RequestParam String token,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {
        TokenValidationResult v = jwtHelper.validateToken(token);
        if (!v.isValid()) {
            return ResultMessage.builder()
                                .code(401)
                                .message("Invalid token")
                                .build();
        }
        String summary = aiSummaryService.generateSummary(token, date);
        return ResultMessage.builder()
                            .code(0)
                            .message("success")
                            .data(summary)
                            .build();
    }

    @ResponseStatus(code = org.springframework.http.HttpStatus.UNAUTHORIZED)
    public static class UnauthorizedException extends RuntimeException {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
