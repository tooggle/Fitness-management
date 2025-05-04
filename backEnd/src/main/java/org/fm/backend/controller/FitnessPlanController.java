package org.fm.backend.controller;

import org.fm.backend.dao.FitnessMapper;
import org.fm.backend.dto.ResultMessage;
import org.fm.backend.service.FitnessService;
import org.fm.backend.service.PhysicalTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/FitnessPlan")
public class FitnessPlanController {

    @Autowired
    FitnessService fitnessService;

    @Autowired
    PhysicalTestService physicalTestService;

    @GetMapping("/PostFitness")
    public ResultMessage postFitness(String token, double height, double weight, double BMI, double bodyFatRate) {
        return fitnessService.postFitness(token, height, weight, BMI, bodyFatRate);
    }

    @GetMapping("/PostPhysicalTest")
    public ResultMessage postPhysicalTest(String token, int pushups, int squats, int situps, int pullup, int longDistance) {
        return physicalTestService.postPhysicalTest(token, pushups, squats, situps, pullup, longDistance);
    }
}
