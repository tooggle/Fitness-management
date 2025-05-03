package org.fm.backend.dao;

import org.apache.ibatis.annotations.*;
import org.fm.backend.model.MealRecords;
import org.fm.backend.model.MealRecordsFood;

import java.util.Date;
import java.util.List;

@Mapper
public interface MealRecordMapper {
    @Insert("INSERT INTO mealrecords (userID, mealTime, mealPhoto) " +
            "VALUES (#{userID}, #{mealTime}, #{mealPhoto})")
    @Options(useGeneratedKeys = true, keyProperty = "recordID", keyColumn = "recordID")
    int insertMealRecord(MealRecords mealRecords);

    @Insert("<script>" +
            "INSERT INTO MealRecordsFood (recordID, foodName, foodAmount) " +
            "VALUES " +
            "<foreach collection='list' item='item' separator=','>" +
            "(#{item.recordID}, #{item.foodName}, #{item.foodAmount})" +
            "</foreach>" +
            "</script>")
    int insertMealRecordsFoods(@Param("list") List<MealRecordsFood> mealRecordsFoods);

    @Select("SELECT * FROM mealrecords WHERE userID = #{userID} AND DATE(mealTime) = #{date}")
    List<MealRecords> getAllMealRecords(@Param("userID") int userID, @Param("date") Date date);

    /**
     * 根据 recordID 获取该饮食记录的详细食物信息
     */
    @Select("SELECT * FROM mealrecordsfood WHERE recordID = #{recordID}")
    List<MealRecordsFood> getMealRecordsFoods(@Param("recordID") int recordID);

    /**
     * 根据 foodName 查询食物的热量
     */
    @Select("SELECT calorie FROM foods WHERE foodName = #{foodName}")
    Integer getFoodCalorieByName(@Param("foodName") String foodName);

    @Update("UPDATE mealrecords SET mealTime=#{mealTime}, mealPhoto=#{mealPhoto} WHERE recordID=#{recordID}")
    boolean updateMealRecordByRecordID(MealRecords mealRecords);

    @Delete("DELETE FROM MealRecordsFood WHERE recordID=#{recordID}")
    boolean deleteMealRecordFoodByID(int recordID);

    @Delete("DELETE FROM mealrecords WHERE recordID=#{recordID}")
    int deleteMealRecordByRecordID(int recordID);
}
