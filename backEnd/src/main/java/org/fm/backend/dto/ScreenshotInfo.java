package org.fm.backend.dto;

public class ScreenshotInfo {
    public int userID ;

    public String exerciseName ;

    public String screenshotUrl ;

    @Override
    public String toString() {
        return "ScreenshotInfo{" +
                "userID=" + userID +
                ", exerciseName='" + exerciseName + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                '}';
    }
}
