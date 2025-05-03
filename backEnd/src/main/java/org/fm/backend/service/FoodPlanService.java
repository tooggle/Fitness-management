package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.FoodPlanMapper;
import org.fm.backend.dto.*;
import org.fm.backend.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class FoodPlanService {
    @Autowired
    private FoodPlanMapper foodPlanMapper;

    @Transactional
    public ResultMealPlan createMealPlan(int userID, MealPlanDTO mealPlan) {
        // 1. 创建 FoodPlan 对象
        FoodPlan foodPlan = new FoodPlan();
        foodPlan.setUserID(userID);
        foodPlan.setCreateTime(new Date());
        foodPlan.setDate(mealPlan.getDate());
        foodPlan.setMealType(mealPlan.getMealType());
        foodPlan.setState(mealPlan.isState());
        foodPlan.setNumsOfTypes(mealPlan.getFoods().size());

        // 插入 FoodPlan 并获取生成的 ID
        int affectedRows = foodPlanMapper.insertFoodPlan(foodPlan);
        if (affectedRows == 0) {
            throw new RuntimeException("Failed to insert food plan");
        }
        int foodPlanID = foodPlan.getFoodPlanID(); // 通过 @Options 自动回填


        // 3. 遍历 foods，创建 MealRecordsFood 对象并保存到数据库
        List<FoodPlanFood> foodPlanFoods = mealPlan.getFoods().stream()
                .map(food -> {
                    FoodPlanFood foodPlanFood = new FoodPlanFood();
                    foodPlanFood.setFoodAmount(food.getQuantity());
                    foodPlanFood.setFoodName(food.getFoodName());
                    foodPlanFood.setFoodPlanID(foodPlanID);
                    return foodPlanFood;
                })
                .toList();

        if (!foodPlanFoods.isEmpty()) {
            int inserted = foodPlanMapper.insertFoodPlanFoods(foodPlanFoods);
            log.info("Inserted {} food items for plan {}", inserted, foodPlanID);
        }

        // 3. 返回响应
        return ResultMealPlan.builder()
                .message("膳食计划创建成功")
                .foodPlanID(foodPlanID)
                .build();
    }

    public GetAllFoodPlanDetailsRes getAllFoodPlans(int userID) {
        // 查询所有餐计划
        List<FoodPlan> plans = foodPlanMapper.getAllFoodPlans(userID);

        // 构建响应对象
        GetAllFoodPlanDetailsRes res = new GetAllFoodPlanDetailsRes();
        List<GetOneFoodPlanDetailRes> planDetails = new ArrayList<>();

        for (FoodPlan plan : plans) {
            GetOneFoodPlanDetailRes single = new GetOneFoodPlanDetailRes();
            single.setFoodPlanID(plan.getFoodPlanID());
            single.setDate(plan.getDate());
            single.setMealType(plan.getMealType());
            single.setState(plan.isState());
            single.setNumOfTypes(plan.getNumsOfTypes());

            // 获取该餐计划的详细食物信息
            List<FoodPlanFood> foodPlanFoods = foodPlanMapper.getFoodPlanFoods(plan.getFoodPlanID());
            List<FoodItemDTO> foods = new ArrayList<>();
            for (FoodPlanFood foodPlanFood : foodPlanFoods) {
                FoodItemDTO food = new FoodItemDTO();
                food.setFoodName(foodPlanFood.getFoodName());
                food.setQuantity(foodPlanFood.getFoodAmount());
                foods.add(food);
            }
            single.setFoods(foods);

            planDetails.add(single);
        }

        res.setPlans(planDetails);
        return res;
    }

    public FoodsRes getALLFoodsInfo() {
        List<Foods> foodInfos = foodPlanMapper.getALLFoodInfoData();

        FoodsRes foodsRes = new FoodsRes();
        List<Foods> foodsInfo = new ArrayList<>();

        for (Foods foodInfo : foodInfos) {
            Foods dtoFoodInfo = new Foods();
            dtoFoodInfo.setFoodName(foodInfo.getFoodName());
            dtoFoodInfo.setCalorie(foodInfo.getCalorie());
            foodsInfo.add(dtoFoodInfo);
        }

        foodsRes.setFoodsInfo(foodsInfo);
        return foodsRes;
    }

    public Recipes getAllRecipes() {
        List<SingleRecipe> recipeList = foodPlanMapper.getAllRecipes();

        Recipes recipes = new Recipes();
        List<SingleRecipe> singleRecipes = new ArrayList<>();

        for (SingleRecipe recipe : recipeList) {
            SingleRecipe singleRecipe = new SingleRecipe();
            singleRecipe.setRecipeID(recipe.getRecipeID());
            singleRecipe.setTitle(recipe.getTitle());
            singleRecipe.setImgUrl(recipe.getImgUrl());
            singleRecipe.setContent(recipe.getContent());
            singleRecipe.setReleaseTime(new Date());
            singleRecipes.add(singleRecipe);
        }

        recipes.setRecipes(singleRecipes);
        return recipes;
    }

    public ResultMessage updateRecipe(UpdateRecipesInfo updateRecipesInfo) {
        ResultMessage res = new ResultMessage();

        if (!foodPlanMapper.updateRecipe(
                updateRecipesInfo.getRecipeID(),
                updateRecipesInfo.getTitle(),
                updateRecipesInfo.getImgUrl(),
                updateRecipesInfo.getContent()
        )) {
            res.setMessage("食谱更新失败！");
            return res;
        }

        res.setMessage("食谱更新成功！");
        return res;
    }
}
