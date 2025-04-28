package org.fm.backend.model;

public class UserFitnessPlan {
    private int userID;
    private String workoutName;
    private int date;
    private int  isCompleted;

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    public int getIsCompleted() {
        return isCompleted;
    }

    public void setIsCompleted(int isCompleted) {
        this.isCompleted = isCompleted;
    }

    @Override
    public String toString() {
        return "UserFitnessPlan{" +
                "userID=" + userID +
                ", workoutName='" + workoutName + '\'' +
                ", date=" + date +
                ", isCompleted=" + isCompleted +
                '}';
    }
}
