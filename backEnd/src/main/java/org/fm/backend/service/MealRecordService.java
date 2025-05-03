package org.fm.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.fm.backend.dao.FoodPlanMapper;
import org.fm.backend.dao.MealRecordMapper;
import org.fm.backend.dto.*;
import org.fm.backend.model.MealRecords;
import org.fm.backend.model.MealRecordsFood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class MealRecordService {
    @Autowired
    private MealRecordMapper mealRecordMapper;
    @Autowired
    private FoodPlanMapper foodPlanMapper; // 注入用于查询热量


    @Transactional
    public MealRecordRes createMealRecord(int userID, CreateMealRecord createMealRecord) {
        // 1. 创建 MealRecords 对象
        MealRecords mealRecords = new MealRecords();
        mealRecords.setUserID(userID);
        mealRecords.setMealTime(createMealRecord.getMealTime());
        mealRecords.setCreateTime(new Date());

        mealRecords.setMealPhoto(createMealRecord.getMealPhoto());

        /*// 处理Base64图片字符串
        String originalPhoto = createMealRecord.getMealPhoto();
        if (originalPhoto != null && originalPhoto.contains("base64,")) {
            // 截取base64,之后的部分
            String base64Data = originalPhoto.substring(originalPhoto.indexOf("base64,") + 7);
            mealRecords.setMealPhoto(base64Data);
        } else {
            // 如果不是Base64格式或为null，保持原样或设为null
            mealRecords.setMealPhoto(originalPhoto);
        }*/

        // 插入 MealRecords 并获取生成的 ID
        int affectedRows = mealRecordMapper.insertMealRecord(mealRecords);
        if (affectedRows == 0) {
            throw new RuntimeException("Failed to insert meal record");
        }
        int recordID = (int) mealRecords.getRecordID(); // 强制类型转换

        // 3. 遍历 foods，创建 MealRecordsFood 对象并保存到数据库
        List<MealRecordsFood> mealRecordsFoods = createMealRecord.getFoods().stream()
                .map(food -> {
                    MealRecordsFood mealRecordsFood = new MealRecordsFood();
                    mealRecordsFood.setFoodAmount(food.getQuantity());
                    mealRecordsFood.setFoodName(food.getFoodName());
                    mealRecordsFood.setRecordID(recordID);
                    return mealRecordsFood;
                })
                .toList();

        if (!mealRecordsFoods.isEmpty()) {
            int inserted = mealRecordMapper.insertMealRecordsFoods(mealRecordsFoods);
            log.info("Inserted {} food items for record {}", inserted, recordID);
        }

        // 4. 计算总热量（假设每个食物的热量已知）
        int totalCalorie = 0; // 这里需要根据实际业务逻辑计算总热量

        for (MealRecordsFood food : mealRecordsFoods) {
            Integer caloriePerUnit = foodPlanMapper.getCalorieByFoodName(food.getFoodName());
            if (caloriePerUnit == null) {
                log.warn("未找到食物 {} 的热量数据", food.getFoodName());
                continue;
            }

            // 假设 quantity 是单位为克，按比例计算热量
            totalCalorie += caloriePerUnit * food.getFoodAmount() / 100; // 按 100g 为标准单位
        }

        // 5. 返回响应
        return MealRecordRes.builder()
                .message("饮食记录创建成功")
                .recordID(recordID)
                .totalCalorie(totalCalorie)
                .build();
    }

    public GetAllMealRecordsDetailsRes getAllDetails(int userID, Date date) {
        List<MealRecords> records = mealRecordMapper.getAllMealRecords(userID, date);

        GetAllMealRecordsDetailsRes res = new GetAllMealRecordsDetailsRes();
        List<GetOneMealRecordDetailsRes> recordDetails = new ArrayList<>();

        for (MealRecords record : records) {
            GetOneMealRecordDetailsRes single = new GetOneMealRecordDetailsRes();
            single.setRecordID(record.getRecordID());
            single.setMealTime(record.getMealTime());
            single.setMealPhoto(record.getMealPhoto());
            single.setCreatedTime(record.getCreateTime());
            single.setDiningAdvice(record.getDiningAdvice());

            List<MealRecordsFood> mealRecordsFoods = mealRecordMapper.getMealRecordsFoods(record.getRecordID());
            List<FoodRecord> foods = new ArrayList<>();
            int totalCalorie = 0;

            for (MealRecordsFood mealRecordsFood : mealRecordsFoods) {
                FoodRecord food = new FoodRecord();
                food.setFoodName(mealRecordsFood.getFoodName());
                food.setQuantity(mealRecordsFood.getFoodAmount());

                int caloriePerUnit = mealRecordMapper.getFoodCalorieByName(mealRecordsFood.getFoodName());
                if (caloriePerUnit > 0) {
                    food.setCalorie(caloriePerUnit * mealRecordsFood.getFoodAmount() / 100);
                    totalCalorie += food.getCalorie();
                }

                foods.add(food);
            }

            single.setFoods(foods);
            single.setTotalCalorie(totalCalorie);

            recordDetails.add(single);
        }

        res.setRecords(recordDetails);
        return res;
    }

    @Transactional
    public ResultMessage updateMealRecords(UpdateMealRecordInfo updateMealRecordInfo) {
        ResultMessage res = new ResultMessage();

        int recordID = updateMealRecordInfo.getRecordID();
        Date mealTime = updateMealRecordInfo.getMealTime();
        String mealPhoto = updateMealRecordInfo.getMealPhoto();

/*        // 处理Base64图片字符串
        if (mealPhoto != null && mealPhoto.contains("base64,")) {
            // 截取base64,之后的部分
            String base64Data = mealPhoto.substring(mealPhoto.indexOf("base64,") + 7);
            mealPhoto = base64Data;
        }*/

        try {// 更新 MealRecords 表
            MealRecords mealRecords = new MealRecords();
            mealRecords.setRecordID(recordID);
            mealRecords.setMealTime(mealTime);
            mealRecords.setMealPhoto(mealPhoto);
            if (!mealRecordMapper.updateMealRecordByRecordID(mealRecords)) {
                res.setMessage("饮食记录更新失败！--更新基本信息时出错！");
                return res;
            }

            // 删除旧的 MealRecordsFood 记录
            if (!mealRecordMapper.deleteMealRecordFoodByID(recordID)) {
                res.setMessage("饮食记录更新失败！--删除旧详细食物信息时出错！");
                return res;
            }

            // 插入新的 MealRecordsFood 记录
            List<MealRecordsFood> mealRecordsFoods = updateMealRecordInfo.getFoods().stream()
                    .map(food -> {
                        MealRecordsFood mealRecordsFood = new MealRecordsFood();
                        mealRecordsFood.setRecordID(recordID);
                        mealRecordsFood.setFoodName(food.getFoodName());
                        mealRecordsFood.setFoodAmount(food.getQuantity());
                        return mealRecordsFood;
                    })
                    .toList();

            if (!mealRecordsFoods.isEmpty() && mealRecordMapper.insertMealRecordsFoods(mealRecordsFoods) == 0) {
                res.setMessage("饮食记录更新失败！--插入新详细食物信息时出错！");
                return res;
            }
            res.setMessage("饮食记录更新成功！");

            return res;

        } catch (Exception ex) {
            res.setMessage("饮食记录更新失败: " + ex.getMessage());
            return res;
        }
    }

    @Transactional
    public ResultMessage deleteMealRecord(int recordID) {
        ResultMessage deleteRes = new ResultMessage();

        try {
            // 删除 MealRecordsFood 表中的相关记录
            boolean deleteFoodRes = mealRecordMapper.deleteMealRecordFoodByID(recordID);
            if (!deleteFoodRes) {
                deleteRes.setMessage("饮食计划删除失败");
                return deleteRes;
            }

            // 删除 MealRecords 表中的记录
            int deleteRecordRes = mealRecordMapper.deleteMealRecordByRecordID(recordID);
            if (deleteRecordRes <= 0) {
                deleteRes.setMessage("饮食计划删除失败");
                return deleteRes;
            }

            deleteRes.setMessage("饮食计划删除成功");
            return deleteRes;

        } catch (Exception ex) {
            deleteRes.setMessage("饮食计划删除失败: " + ex.getMessage());
            return deleteRes;
        }
    }
}
