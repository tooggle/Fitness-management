package org.fm.backend.model;

import java.util.Date;

public class VigorTokenRecord {
    private int recordID;
    private int userID;
    private String reason;
    private int change;
    private int balance;
    private Date createTime;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public int getRecordID() {
        return recordID;
    }

    public void setRecordID(int recordID) {
        this.recordID = recordID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getChange() {
        return change;
    }

    public void setChange(int change) {
        this.change = change;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "VigorTokenRecord{" +
                "recordID=" + recordID +
                ", userID=" + userID +
                ", reason='" + reason + '\'' +
                ", change=" + change +
                ", balance=" + balance +
                ", createTime=" + createTime +
                '}';
    }
}
