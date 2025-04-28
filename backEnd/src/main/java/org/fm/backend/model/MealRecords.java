package org.fm.backend.model;

import java.util.Date;

public class MealRecords {
    private int recordID;
    private int userID;
    private Date createTime;
    private Date mealTime;
    private String mealPhoto;
    private String diningAdvice;

    public Date getMealTime() {
        return mealTime;
    }

    public void setMealTime(Date mealTime) {
        this.mealTime = mealTime;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
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

    public String getMealPhoto() {
        return mealPhoto;
    }

    public void setMealPhoto(String mealPhoto) {
        this.mealPhoto = mealPhoto;
    }

    public String getDiningAdvice() {
        return diningAdvice;
    }

    public void setDiningAdvice(String diningAdvice) {
        this.diningAdvice = diningAdvice;
    }

    @Override
    public String toString() {
        return "MealRecords{" +
                "recordID=" + recordID +
                ", userID=" + userID +
                ", createTime=" + createTime +
                ", mealTime=" + mealTime +
                ", mealPhoto='" + mealPhoto + '\'' +
                ", diningAdvice='" + diningAdvice + '\'' +
                '}';
    }
}
