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
import java.util.Map;

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

    @Transactional
    public ResultMessage updateFoodPlanState(int foodPlanID, int state) {
        ResultMessage res = new ResultMessage();

        System.out.println("更新状态" + foodPlanID);
        if (!foodPlanMapper.updateFoodPlanState(foodPlanID, state)) {
            res.setMessage("饮食计划状态更新失败！(12)");
            return res;
        }

        res.setMessage("饮食计划状态更新成功！");

        // 确保每条饮食计划只能更新一次成就进度
        Map<String, Object> foodPlanInfo = foodPlanMapper.getFoodPlanAchieGain(foodPlanID);
        int haveGainAchievement = (Integer) foodPlanInfo.get("achievementGain");
        int userID = (Integer) foodPlanInfo.get("userID");

        // 完成饮食计划更新成就进度
        if (state == 1 && haveGainAchievement == 0) {
            foodPlanMapper.updateAchievementGain(foodPlanID, 1);
            // 假设 UserAchievementService 是处理用户成就的 Service
            UserAchievementService userAchievementService = new UserAchievementService();
            userAchievementService.updateFoodPlanAchievement(userID);
        }
        return res;
    }

    @Transactional
    public ResultMessage deleteFoodPlan(int foodPlanID) {
        ResultMessage deleteFoodPlanRes = new ResultMessage();

        boolean res = foodPlanMapper.deleteFoodPlanByPlanID(foodPlanID);

        if (res) {
            deleteFoodPlanRes.setMessage("饮食计划删除成功");
        } else {
            deleteFoodPlanRes.setMessage("饮食计划删除失败");
        }

        return deleteFoodPlanRes;
    }

    @Transactional
    public ResultMessage updateFoodPlan(UpdateFoodPlanInfo updateFoodPlanInfo) {
        ResultMessage res = new ResultMessage();

        int foodPlanID = updateFoodPlanInfo.getFoodPlanID();
        int mealType = updateFoodPlanInfo.getMealType();
        int numOfTypes = updateFoodPlanInfo.getFoods().size();

        try {
            // 更新 FoodPlan 表
            if (!foodPlanMapper.updateFoodPlanByPlanID(foodPlanID, mealType, numOfTypes)) {
                res.setMessage("饮食计划更新失败:缩略表信息更新错误！");
                return res;
            }

            // 删除旧的 FoodPlanFood 记录
            if (!foodPlanMapper.deleteFoodPlanFoodByPlanID(foodPlanID)) {
                res.setMessage("饮食计划更新失败:旧食物详情表删除失败！");
                return res;
            }

            // 插入新的 FoodPlanFood 记录
            for (FoodItemDTO foodItem : updateFoodPlanInfo.getFoods()) {
                FoodPlanFood foodPlanFood = new FoodPlanFood();
                foodPlanFood.setFoodPlanID(foodPlanID);
                foodPlanFood.setFoodName(foodItem.getFoodName());
                foodPlanFood.setFoodAmount(foodItem.getQuantity());

                if (!foodPlanMapper.insertFoodPlanFood(foodPlanFood)) {
                    res.setMessage("饮食计划更新失败:新食物详情信息插入失败！");
                    return res;
                }
            }

            res.setMessage("饮食计划更新成功！");
            return res;
        } catch (Exception ex) {
            res.setMessage("饮食计划更新失败:" + ex.getMessage());
            return res;
        }
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

        Date releaseTime = new Date();
        if (!foodPlanMapper.updateRecipe(
                updateRecipesInfo.getRecipeID(),
                updateRecipesInfo.getTitle(),
                updateRecipesInfo.getImgUrl(),
                updateRecipesInfo.getContent(),
                releaseTime
        )) {
            res.setMessage("食谱更新失败！");
            return res;
        }

        res.setMessage("食谱更新成功！");
        return res;
    }

    @Transactional
    public RecipesRes insertRecipe(RecipesInfo recipesInfo) {
        RecipesRes recipesRes = RecipesRes.builder()
                .message("食谱插入失败！")
                .recipeID(-1)
                .releaseTime(new Date())
                .build();

        /*String imgurl = recipesInfo.getImgUrl();
        // 处理 imgUrl
        if (imgurl != null && imgurl.contains("base64,")) {
            // 截取base64,之后的部分
            String base64Data = imgurl.substring(imgurl.indexOf("base64,") + 7);
            recipesInfo.setImgUrl(base64Data);
        }*/

        Date releaseTime = new Date();
        int recipeID = foodPlanMapper.insertRecipe(recipesInfo, releaseTime);

        if (recipeID == -1) {
            return recipesRes;
        } else {
            recipesRes.setMessage("食谱插入成功！");
            recipesRes.setRecipeID(recipeID);
            recipesRes.setReleaseTime(releaseTime);
            return recipesRes;
        }
    }

    @Transactional
    public boolean deleteRecipeByID(int recipeID) {
        try {
            // 调用 Mapper 方法删除食谱
            int affectedRows = foodPlanMapper.deleteRecipeByID(recipeID);
            return affectedRows > 0; // 如果受影响的行数大于0，表示删除成功
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    @Transactional
    public ResultMessage insertFoodInfo(Foods foodInfo) {
        ResultMessage res = new ResultMessage();

        int affectedRows = foodPlanMapper.insertFoodInfo(foodInfo);
        if (affectedRows > 0) {
            res.setMessage("食物信息插入成功！");
        } else {
            res.setMessage("食物信息插入失败！");
        }
        return res;
    }

    public ResultMessage updateFoodInfo(Foods foodInfo) {
        ResultMessage res = new ResultMessage();

        int affectedRows = foodPlanMapper.updateFoodInfo(foodInfo);
        if (affectedRows > 0) {
            res.setMessage("食物信息更新成功！");
        } else {
            res.setMessage("食物信息更新失败！");
        }
        return res;
    }

    public ResultMessage deleteFoodInfo(String foodName) {
        ResultMessage res = new ResultMessage();

        int affectedRows = foodPlanMapper.deleteFoodInfo(foodName);
        if (affectedRows > 0) {
            res.setMessage("食物信息删除成功！");
        } else {
            res.setMessage("食物信息删除失败！");
        }
        return res;
    }
}
