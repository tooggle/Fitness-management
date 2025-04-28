package org.fm.backend.model;

import java.util.Date;

public class Fitness {
    private int userID;
    private Date createTime;
    private double height;
    private double weight;
    private double BMI;
    private double bodyFatRate;//体脂率，选填

    public double getBMI() {
        return BMI;
    }

    public void setBMI(double BMI) {
        this.BMI = BMI;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getBodyFatRate() {
        return bodyFatRate;
    }

    public void setBodyFatRate(double bodyFatRate) {
        this.bodyFatRate = bodyFatRate;
    }

    @Override
    public String toString() {
        return "Fitness{" +
                "userID=" + userID +
                ", createTime=" + createTime +
                ", height=" + height +
                ", weight=" + weight +
                ", BMI=" + BMI +
                ", bodyFatRate=" + bodyFatRate +
                '}';
    }
}
