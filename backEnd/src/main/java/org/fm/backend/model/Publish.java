package org.fm.backend.model;

import java.util.Date;

public class Publish {
    private int postID ;
    private int userID ;
    private Date publishTime ;

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    @Override
    public String toString() {
        return "Publish{" +
                "postID=" + postID +
                ", userID=" + userID +
                ", publishTime=" + publishTime +
                '}';
    }
}
