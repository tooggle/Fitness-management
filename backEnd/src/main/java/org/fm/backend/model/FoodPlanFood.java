package org.fm.backend.model;

import lombok.Data;

@Data
public class FoodPlanFood {
    private int foodPlanID;
    private String foodName;
    private int foodAmount;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getFoodPlanID() {
        return foodPlanID;
    }

    public void setFoodPlanID(int foodPlanID) {
        this.foodPlanID = foodPlanID;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    @Override
    public String toString() {
        return "FoodPlanFood{" +
                "foodPlanID=" + foodPlanID +
                ", foodName='" + foodName + '\'' +
                ", foodAmount=" + foodAmount +
                '}';
    }
}
