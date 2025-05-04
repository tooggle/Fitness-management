package org.fm.backend.model;

import lombok.Data;

@Data
public class Foods {
    private String foodName ;
    private int calorie ;

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    @Override
    public String toString() {
        return "Foods{" +
                "foodName='" + foodName + '\'' +
                ", calorie=" + calorie +
                '}';
    }
}
