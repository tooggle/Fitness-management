package org.fm.backend.model;

public class Workout {
    private String workoutName;
    private String exerciseName;
    private String coverUrl;

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public String getWorkoutName() {
        return workoutName;
    }

    public void setWorkoutName(String workoutName) {
        this.workoutName = workoutName;
    }

    @Override
    public String toString() {
        return "Workout{" +
                "workoutName='" + workoutName + '\'' +
                ", exerciseName='" + exerciseName + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
