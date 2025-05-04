package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.*;
import org.fm.backend.util.JWTHelper;
import org.fm.backend.service.MealRecordService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@Slf4j
@RestController
@RequestMapping("/api/MealRecords")
public class MealRecordController {
    @Autowired
    private MealRecordService mealRecordService;
    @Autowired
    private JWTHelper jwtHelper;

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
    public AIRes aISuggestions(@RequestParam int recordID){
        return new AIRes();
    }

    @GetMapping("/GetAISummary")
    public ResultMessage getAISummary(@RequestParam String token,
                              @RequestParam  @DateTimeFormat(pattern = "yyyy-MM-dd") Date date
                              ){
        return new ResultMessage();
    }
}
