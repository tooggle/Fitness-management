package org.fm.backend.model;

public class Teaches {
    private int coachID ;
    private int classID ;
    private int typeID ;

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public int getClassID() {
        return classID;
    }

    public void setClassID(int classID) {
        this.classID = classID;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    @Override
    public String toString() {
        return "Teaches{" +
                "coachID=" + coachID +
                ", classID=" + classID +
                ", typeID=" + typeID +
                '}';
    }
}
