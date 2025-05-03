package org.fm.backend.model;

public class Advise {
    private int classID ;
    private int coachID ;
    private int traineeID ;

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    @Override
    public String toString() {
        return "Advise{" +
                "classID=" + classID +
                ", coachID=" + coachID +
                ", traineeID=" + traineeID +
                '}';
    }
}
