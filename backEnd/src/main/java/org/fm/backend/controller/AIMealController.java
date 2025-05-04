package org.fm.backend.controller;

import org.fm.backend.dto.AIRes;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.service.MealAIGuideService;
import org.fm.backend.service.AISummaryService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/api/ai/meal")
public class AiMealController {

    private final MealAIGuideService mealAIGuideService;
    private final AISummaryService summaryService;

    public AiMealController(MealAIGuideService mealAIGuideService,
                            AISummaryService summaryService) {
        this.mealAIGuideService = mealAIGuideService;
        this.summaryService = summaryService;
    }

    @GetMapping("/suggestions")
    public AIRes mealSuggestions(@RequestParam("recordId") Long recordId) {
        String suggestion = mealAIGuideService.generateMealAdvice(recordId);
        return AIRes.builder()
                    .suggestion(suggestion)
                    .build();
    }

    @GetMapping("/summary")
    public ResultMessage mealSummary(
            @RequestParam("token") String token,
            @RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        // Optionally, validate token inside summaryService or here
        String summary = summaryService.generateSummary(token, date);
        return ResultMessage.builder()
                            .code(0)
                            .message("success")
                            .data(summary)
                            .build();
    }
}
