package org.fm.backend.model;

public class Participate {
    private int traineeID;    // 学员ID（外键）
    private int classID;      // 课程班级ID（外键）
    private int typeID;       // 课程类型ID（外键）
    private int Grade ;   // 评分）
    private String Evaluate ; // 评价

    public String getEvaluate() {
        return Evaluate;
    }

    public void setEvaluate(String evaluate) {
        Evaluate = evaluate;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }

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

    public int getTraineeID() {
        return traineeID;
    }

    public void setTraineeID(int traineeID) {
        this.traineeID = traineeID;
    }

    @Override
    public String toString() {
        return "Participate{" +
                "traineeID=" + traineeID +
                ", classID=" + classID +
                ", typeID=" + typeID +
                ", Grade=" + Grade +
                ", Evaluate='" + Evaluate + '\'' +
                '}';
    }
}

