package org.fm.backend.model;

public class FitnessSuggestion {
    private int screenshotID;
    private int userID;
    private String createTime;
    private String exerciseName;
    private String suggestions;
    private String screenshotUrl;

    public String getScreenshotUrl() {
        return screenshotUrl;
    }

    public void setScreenshotUrl(String screenshotUrl) {
        this.screenshotUrl = screenshotUrl;
    }

    public String getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(String suggestions) {
        this.suggestions = suggestions;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getScreenshotID() {
        return screenshotID;
    }

    public void setScreenshotID(int screenshotID) {
        this.screenshotID = screenshotID;
    }

    @Override
    public String toString() {
        return "FitnessSuggestion{" +
                "screenshotID=" + screenshotID +
                ", userID=" + userID +
                ", createTime='" + createTime + '\'' +
                ", exerciseName='" + exerciseName + '\'' +
                ", suggestions='" + suggestions + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                '}';
    }
}
