package org.fm.backend.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.fm.backend.dto.*;
import org.fm.backend.model.Foods;
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

    @PutMapping("/UpdateState")
    public ResultMessage updateFoodPlanState(@RequestBody UpdateStateDTO updateStateDTO) {
        log.info("更新饮食计划状态，{}", updateStateDTO);
        return foodPlanService.updateFoodPlanState(updateStateDTO.getFoodPlanID(), updateStateDTO.isState() ? 1 : 0);
    }

    @DeleteMapping("/Delete")
    public ResultMessage deleteFoodPlan(@RequestParam int foodPlanID) {
        log.info("删除饮食计划，foodPlanID: {}", foodPlanID);
        return foodPlanService.deleteFoodPlan(foodPlanID);
    }

    @PutMapping("/Update")
    public ResultMessage updateFoodPlan(@RequestBody UpdateFoodPlanInfo updateFoodPlanInfo) {
        log.info("更新饮食计划，{}", updateFoodPlanInfo);
        return foodPlanService.updateFoodPlan(updateFoodPlanInfo);
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

    @PostMapping("/InsertRecipe")
    public RecipesRes insertRecipe(@RequestBody RecipesInfo recipesInfo) {
        log.info("插入食谱，{}", recipesInfo);
        return foodPlanService.insertRecipe(recipesInfo);
    }

    @DeleteMapping("/DeleteRecipe")
    public ResultMessage deleteRecipe(@RequestParam int recipeID) {
        ResultMessage res = new ResultMessage();

        try {
            boolean result = foodPlanService.deleteRecipeByID(recipeID);
            if (result) {
                res.setMessage("食谱删除成功！");
            } else {
                res.setMessage("食谱删除失败！");
            }
        } catch (Exception ex) {
            res.setMessage("食谱删除失败：" + ex.getMessage());
        }

        return res;
    }

    @PostMapping("/InsertFoodInfo")
    public ResultMessage insertFoodInfo(@RequestBody Foods foodInfo) {
        log.info("插入食物信息，{}", foodInfo);
        return foodPlanService.insertFoodInfo(foodInfo);
    }

    @PutMapping("/UpdateFoodInfo")
    public ResultMessage updateFoodInfo(@RequestBody Foods foodInfo) {
        log.info("更新食物信息，{}", foodInfo);
        return foodPlanService.updateFoodInfo(foodInfo);
    }

    @DeleteMapping("/DeleteFoodInfo")
    public ResultMessage deleteFoodInfo(String foodName) {
        log.info("删除食物信息，{}", foodName);
        return foodPlanService.deleteFoodInfo(foodName);
    }
}
