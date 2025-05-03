package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dto.*;
import org.fm.backend.model.Recipes;
import org.fm.backend.service.FoodPlanService;
import org.fm.backend.util.JWTHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequestMapping("/api/MealPlans")
public class FoodPlanController {
    @Autowired
    private FoodPlanService foodPlanService;
    @Autowired
    private JWTHelper jwtHelper;

    @PostMapping("/Create")
    public ResultMealPlan createMealPlan(@RequestParam String token, @RequestBody MealPlanDTO mealPlan) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            return ResultMealPlan.builder()
                    .message("Invalid token")
                    .foodPlanID(0)
                    .build();
        }
        int userID = validationResult.getUserID();
        log.info("膳食计划创建成功");
        return foodPlanService.createMealPlan(userID,mealPlan);
    }

    @GetMapping("/GetAllDetails")
    public GetAllFoodPlanDetailsRes getAllFoodPlans(@RequestParam String token) {
        TokenValidationResult validationResult = jwtHelper.validateToken(token);
        if (!validationResult.IsValid) {
            throw new RuntimeException("Invalid token");
        }
        int userID = validationResult.getUserID();
        return foodPlanService.getAllFoodPlans(userID);
    }

    @GetMapping("/GetFoodsInfo")
    public FoodsRes getALLFoodsInfo() {
        return foodPlanService.getALLFoodsInfo();
    }

    @GetMapping("/GetAllRecipes")
    public Recipes getAllRecipes() {
        return foodPlanService.getAllRecipes();
    }

    @PutMapping("/UpdateRecipe")
    public ResultMessage updateRecipe(@RequestBody UpdateRecipesInfo updateRecipesInfo) {
        log.info("更新食谱，{}",updateRecipesInfo);
        return foodPlanService.updateRecipe(updateRecipesInfo);
    }
}
