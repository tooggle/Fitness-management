package org.fm.backend.model;

import java.util.Date;

public class Update {
    private int coachID;
    private int classID;
    private String actionType;
    private Date updateTime;
    private String updateContext;

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

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

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getUpdateContext() {
        return updateContext;
    }

    public void setUpdateContext(String updateContext) {
        this.updateContext = updateContext;
    }

    @Override
    public String toString() {
        return "Update{" +
                "coachID=" + coachID +
                ", classID=" + classID +
                ", actionType='" + actionType + '\'' +
                ", updateTime=" + updateTime +
                ", updateContext='" + updateContext + '\'' +
                '}';
    }
}
