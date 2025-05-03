package org.fm.backend.model;

import java.util.Date;

public class PhysicalTest {
    private int userID;
    private int pushups;//俯卧撑次数
    private int squats;//深蹲次数
    private int situps;//仰卧起坐次数
    private int pullup;//引体向上次数
    private int longDistance;//一千米时间

    public int getSitups() {
        return situps;
    }

    public void setSitups(int situps) {
        this.situps = situps;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPushups() {
        return pushups;
    }

    public void setPushups(int pushups) {
        this.pushups = pushups;
    }

    public int getSquats() {
        return squats;
    }

    public void setSquats(int squats) {
        this.squats = squats;
    }

    public int getPullup() {
        return pullup;
    }

    public void setPullup(int pullup) {
        this.pullup = pullup;
    }

    public int getLongDistance() {
        return longDistance;
    }

    public void setLongDistance(int longDistance) {
        this.longDistance = longDistance;
    }

    @Override
    public String toString() {
        return "PhysicalTest{" +
                "userID=" + userID +
                ", pushups=" + pushups +
                ", squats=" + squats +
                ", situps=" + situps +
                ", pullup=" + pullup +
                ", longDistance=" + longDistance +
                '}';
    }
}
