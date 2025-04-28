package org.fm.backend.model;

import java.util.Date;

public class Posts {
    private int postID;
    private int userID;
    private String userName;
    private String postTitle;
    private String postContent;
    private String postCategory;
    private Date postTime;
    private int likesCount;
    private int forwardCount;
    private int commentsCount;
    private int referencePostID;
    private String imgUrl;
    private int isPinned;
    private int isReported;

    public String getPostCategory() {
        return postCategory;
    }

    public void setPostCategory(String postCategory) {
        this.postCategory = postCategory;
    }

    public int getPostID() {
        return postID;
    }

    public void setPostID(int postID) {
        this.postID = postID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public Date getPostTime() {
        return postTime;
    }

    public void setPostTime(Date postTime) {
        this.postTime = postTime;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getForwardCount() {
        return forwardCount;
    }

    public void setForwardCount(int forwardCount) {
        this.forwardCount = forwardCount;
    }

    public int getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    public int getReferencePostID() {
        return referencePostID;
    }

    public void setReferencePostID(int referencePostID) {
        this.referencePostID = referencePostID;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(int isPinned) {
        this.isPinned = isPinned;
    }

    public int getIsReported() {
        return isReported;
    }

    public void setIsReported(int isReported) {
        this.isReported = isReported;
    }

    @Override
    public String toString() {
        return "Posts{" +
                "postID=" + postID +
                ", userID=" + userID +
                ", userName='" + userName + '\'' +
                ", postTitle='" + postTitle + '\'' +
                ", postContent='" + postContent + '\'' +
                ", postCategory='" + postCategory + '\'' +
                ", postTime=" + postTime +
                ", likesCount=" + likesCount +
                ", forwardCount=" + forwardCount +
                ", commentsCount=" + commentsCount +
                ", referencePostID=" + referencePostID +
                ", imgUrl='" + imgUrl + '\'' +
                ", isPinned=" + isPinned +
                ", isReported=" + isReported +
                '}';
    }
}
