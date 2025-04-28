package org.fm.backend.model;

public class Exercise {
    private String exerciseName;
    private String explanation;
    private String gifUrl;
    private String equipmentName;
    private String part;
    private int time;
    private int count;
    private String coverUrl;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getGifUrl() {
        return gifUrl;
    }

    public void setGifUrl(String gifUrl) {
        this.gifUrl = gifUrl;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "Exercise{" +
                "exerciseName='" + exerciseName + '\'' +
                ", explanation='" + explanation + '\'' +
                ", gifUrl='" + gifUrl + '\'' +
                ", equipmentName='" + equipmentName + '\'' +
                ", part='" + part + '\'' +
                ", time=" + time +
                ", count=" + count +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
