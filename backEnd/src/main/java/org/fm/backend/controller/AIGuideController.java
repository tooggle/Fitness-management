package org.fm.backend.controller;

import org.fm.backend.dto.*;
import org.fm.backend.model.FitnessSuggestion;
import org.fm.backend.service.FitnessAIGuideService;
import org.fm.backend.util.JWTHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/AIGuide")
public class AIGuideController {

    @Autowired
    private JWTHelper jwtHelper;

    @Autowired
    private FitnessAIGuideService fitnessAIGuideService;

    @PostMapping("/Create")
    public ScreenshotRes Create(String token, @RequestBody CreateScreenshot screenshot) throws IOException {
        TokenValidationResult result = jwtHelper.validateToken(token);
        int userID = result.userID;
        ScreenshotInfo screenshotInfo = new ScreenshotInfo();
        screenshotInfo.userID = userID;
        screenshotInfo.screenshotUrl = screenshot.screenshotUrl;
        screenshotInfo.exerciseName = screenshot.exerciseName;
        System.out.println(screenshot);
        return fitnessAIGuideService.create(screenshotInfo);
    }

    @GetMapping("/GetAISuggestion")
    public ResultMessage GetAISuggestion(int screenshotID)  {
        return fitnessAIGuideService.getAISuggestion(screenshotID);
    }

    @GetMapping("/GetAllDetails")
    public List<FitnessSuggestion> GetAllDetails(String token) {
        TokenValidationResult result = jwtHelper.validateToken(token);
        return fitnessAIGuideService.getAllDetails(result.userID);
    }

    @DeleteMapping("/Delete")
    public ResultMessage Delete(int screenshotID)  {
        return fitnessAIGuideService.deleteFitnessSuggestion(screenshotID);
    }
}
