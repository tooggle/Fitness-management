package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.FoodPlan;
import org.fm.backend.model.FoodPlanFood;
import org.fm.backend.model.Foods;
import org.fm.backend.model.SingleRecipe;

import java.util.List;

@Mapper
public interface FoodPlanMapper {
    @Insert("INSERT INTO foodplan (userID, createTime, date, mealType, state, numOfTypes) " +
            "VALUES (#{userID}, #{createTime}, #{date}, #{mealType}, " +
            "#{state, jdbcType=BOOLEAN}, #{numsOfTypes})")  // 使用jdbcType明确类型
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

    @Update("<script>" +
            "UPDATE recipes SET " +
            "<set> " +
            "title = #{title}, " +
            "imgUrl = #{imgUrl}, " +
            "content = #{content} " +
            "</set> " +
            "WHERE recipeID = #{recipeID}" +
            "</script>")
    boolean updateRecipe(
            @Param("recipeID") int recipeID,
            @Param("title") String title,
            @Param("imgUrl") String imgUrl,
            @Param("content") String content
    );

    @Select("SELECT calorie FROM foods WHERE foodName = #{foodName}")
    Integer getCalorieByFoodName(@Param("foodName") String foodName);

}
