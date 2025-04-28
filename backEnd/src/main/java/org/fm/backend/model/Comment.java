package org.fm.backend.model;

import java.util.Date;

public class Comment {
    private int commentID;
    private int userID;
    private String userName;
    private int postID;
    private int parentCommentID; // 父评论ID（如果是根评论则为-1）
    private Date commentTime;
    private int likesCount;
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public Date getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(Date commentTime) {
        this.commentTime = commentTime;
    }

    public int getParentCommentID() {
        return parentCommentID;
    }

    public void setParentCommentID(int parentCommentID) {
        this.parentCommentID = parentCommentID;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getCommentID() {
        return commentID;
    }

    public void setCommentID(int commentID) {
        this.commentID = commentID;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentID=" + commentID +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", postID=" + postID +
                ", parentCommentID=" + parentCommentID +
                ", commentTime=" + commentTime +
                ", likesCount=" + likesCount +
                ", content='" + content + '\'' +
                '}';
    }
}
