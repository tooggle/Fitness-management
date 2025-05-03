package org.fm.backend.model;

public class MealRecordsFood {
    private int recordID ;

    private String foodName ;

    private int foodAmount ;

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    @Override
    public String toString() {
        return "MealRecordsFood{" +
                "recordID=" + recordID +
                ", foodName='" + foodName + '\'' +
                ", foodAmount=" + foodAmount +
                '}';
    }
}
