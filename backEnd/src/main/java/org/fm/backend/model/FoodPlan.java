package org.fm.backend.model;

import java.util.Date;

public class FoodPlan {
    private int foodPlanID;
    private int userID;
    private Date createTime;
    private Date date;
    private int mealType;
    private boolean state;
    private int numsOfTypes;
    private int achievementGain;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getFoodPlanID() {
        return foodPlanID;
    }

    public void setFoodPlanID(int foodPlanID) {
        this.foodPlanID = foodPlanID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getMealType() {
        return mealType;
    }

    public void setMealType(int mealType) {
        this.mealType = mealType;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public int getNumsOfTypes() {
        return numsOfTypes;
    }

    public void setNumsOfTypes(int numsOfTypes) {
        this.numsOfTypes = numsOfTypes;
    }

    public int getAchievementGain() {
        return achievementGain;
    }

    public void setAchievementGain(int achievementGain) {
        this.achievementGain = achievementGain;
    }

    @Override
    public String toString() {
        return "FoodPlan{" +
                "foodPlanID=" + foodPlanID +
                ", userID=" + userID +
                ", createTime=" + createTime +
                ", date=" + date +
                ", mealType=" + mealType +
                ", state=" + state +
                ", numsOfTypes=" + numsOfTypes +
                ", achievementGain=" + achievementGain +
                '}';
    }
}
