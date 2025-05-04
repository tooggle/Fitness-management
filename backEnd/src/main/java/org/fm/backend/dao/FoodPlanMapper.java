package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.dto.RecipesInfo;
import org.fm.backend.model.FoodPlan;
import org.fm.backend.model.FoodPlanFood;
import org.fm.backend.model.Foods;
import org.fm.backend.model.SingleRecipe;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Mapper
public interface FoodPlanMapper {
    @Insert("INSERT INTO foodplan (userID, createTime, date, mealType, state, numOfTypes, achievementGain) " +
            "VALUES (#{userID}, #{createTime}, #{date}, #{mealType}, " +
            "#{state, jdbcType=BOOLEAN}, #{numsOfTypes}, #{achievementGain})")  // 使用jdbcType明确类型
    @Options(useGeneratedKeys = true, keyProperty = "foodPlanID", keyColumn = "foodPlanID")
    int insertFoodPlan(FoodPlan foodPlan);

    @Insert("<script>" +
            "INSERT INTO FoodPlanFood (foodPlanID, foodName, foodAmount) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.foodPlanID}, #{item.foodName}, #{item.foodAmount})" +
            "</foreach>" +
            "</script>")
    int insertFoodPlanFoods(@Param("list") List<FoodPlanFood> foodPlanFoods);

    @Select("SELECT * FROM foodplan WHERE userID = #{userID}")
    List<FoodPlan> getAllFoodPlans(@Param("userID") int userID);

    @Select("SELECT * FROM foodplanfood WHERE foodPlanID = #{foodPlanID}")
    List<FoodPlanFood> getFoodPlanFoods(@Param("foodPlanID") int foodPlanID);

    @Select("SELECT * FROM foods")
    List<Foods> getALLFoodInfoData();

    @Select("SELECT * FROM recipes")
    List<SingleRecipe> getAllRecipes();

    @Update("UPDATE recipes " +
            "SET title = #{title}, " +
            "imgUrl = #{imgUrl}, " +
            "content = #{content}, " +
            "releaseTime = #{releaseTime} " +
            "WHERE recipeID = #{recipeID}")
    boolean updateRecipe(
            @Param("recipeID") int recipeID,
            @Param("title") String title,
            @Param("imgUrl") String imgUrl,
            @Param("content") String content,
            @Param("releaseTime") Date releaseTime
    );


    @Select("SELECT calorie FROM foods WHERE foodName = #{foodName}")
    Integer getCalorieByFoodName(@Param("foodName") String foodName);

    @Insert("INSERT INTO recipes (title, imgUrl, content, releaseTime) " +
            "VALUES (#{recipesInfo.title}, #{recipesInfo.imgUrl}, #{recipesInfo.content}, #{releaseTime})")
    @Options(useGeneratedKeys = true, keyProperty = "recipesInfo.recipeID", keyColumn = "recipeID")
    int insertRecipe(@Param("recipesInfo") RecipesInfo recipesInfo, @Param("releaseTime") Date releaseTime);

    @Delete("DELETE FROM recipes WHERE recipeID = #{recipeID}")
    int deleteRecipeByID(@Param("recipeID") int recipeID);

    @Insert("INSERT INTO foods (foodName, calorie) VALUES (#{foodName}, #{calorie})")
    int insertFoodInfo(Foods foodInfo);

    @Update("UPDATE foods SET foodName = #{foodName}, calorie = #{calorie} WHERE foodName = #{foodName}")
    int updateFoodInfo(Foods foodInfo);

    @Delete("DELETE FROM foods WHERE foodName = #{foodName}")
    int deleteFoodInfo(String foodName);

    @Update("UPDATE foodplan SET state = #{state} WHERE foodPlanID = #{foodPlanID}")
    boolean updateFoodPlanState(@Param("foodPlanID") int foodPlanID, @Param("state") int state);
    @Select("SELECT userID, achievementGain FROM foodplan WHERE foodPlanID = #{foodPlanID}")
    Map<String, Object> getFoodPlanAchieGain(@Param("foodPlanID") int foodPlanID);

    @Update("UPDATE foodplan SET achievementGain = #{achievementGain} WHERE foodPlanID = #{foodPlanID}")
    void updateAchievementGain(@Param("foodPlanID") int foodPlanID, @Param("achievementGain") int achievementGain);

    @Delete("DELETE FROM foodplan WHERE foodPlanID = #{foodPlanID}")
    boolean deleteFoodPlanByPlanID(@Param("foodPlanID") int foodPlanID);

    @Update("UPDATE foodplan SET mealType = #{mealType}, numOfTypes = #{numOfTypes} WHERE foodPlanID = #{foodPlanID}")
    boolean updateFoodPlanByPlanID(
            @Param("foodPlanID") int foodPlanID,
            @Param("mealType") int mealType,
            @Param("numOfTypes") int numOfTypes
    );
    @Delete("DELETE FROM FoodPlanFood WHERE foodPlanID = #{foodPlanID}")
    boolean deleteFoodPlanFoodByPlanID(@Param("foodPlanID") int foodPlanID);

    @Insert("INSERT INTO FoodPlanFood (foodPlanID, foodName, foodAmount) VALUES (#{foodPlanID}, #{foodName}, #{foodAmount})")
    boolean insertFoodPlanFood(FoodPlanFood foodPlanFood);


}
