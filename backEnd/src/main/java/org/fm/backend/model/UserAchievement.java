package org.fm.backend.model;

public class UserAchievement {
    private int userID;
    private int achievementID;
    private int progress;
    private String achievedDate;
    private int isAchieved;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getAchievementID() {
        return achievementID;
    }

    public void setAchievementID(int achievementID) {
        this.achievementID = achievementID;
    }

    public String getAchievedDate() {
        return achievedDate;
    }

    public void setAchievedDate(String achievedDate) {
        this.achievedDate = achievedDate;
    }

    public int getIsAchieved() {
        return isAchieved;
    }

    public void setIsAchieved(int isAchieved) {
        this.isAchieved = isAchieved;
    }

    @Override
    public String toString() {
        return "UserAchievement{" +
                "userID=" + userID +
                ", achievementID=" + achievementID +
                ", progress=" + progress +
                ", achievedDate='" + achievedDate + '\'' +
                ", isAchieved=" + isAchieved +
                '}';
    }
}
