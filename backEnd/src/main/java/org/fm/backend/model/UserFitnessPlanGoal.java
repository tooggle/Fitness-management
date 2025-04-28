package org.fm.backend.model;

public class UserFitnessPlanGoal {
    private int userID;
    private int duration;
    private String planType;

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    @Override
    public String toString() {
        return "UserFitnessPlanGoal{" +
                "userID=" + userID +
                ", duration=" + duration +
                ", planType='" + planType + '\'' +
                '}';
    }
}
