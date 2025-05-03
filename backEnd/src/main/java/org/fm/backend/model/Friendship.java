package org.fm.backend.model;

import java.util.Date;

public class Friendship {
    private int userID;
    private int friendID;

    public int getFriendID() {
        return friendID;
    }

    public void setFriendID(int friendID) {
        this.friendID = friendID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "userID=" + userID +
                ", friendID=" + friendID +
                '}';
    }
}
