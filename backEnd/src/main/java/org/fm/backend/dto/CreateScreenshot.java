package org.fm.backend.dto;

public class CreateScreenshot {
    public String exerciseName ;

    public String screenshotUrl ;

    @Override
    public String toString() {
        return "CreateScreenshot{" +
                "exerciseName='" + exerciseName + '\'' +
                ", screenshotUrl='" + screenshotUrl + '\'' +
                '}';
    }
}
