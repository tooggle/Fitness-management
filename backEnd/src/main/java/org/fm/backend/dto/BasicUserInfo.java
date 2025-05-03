package org.fm.backend.dto;



import java.util.Date;

public class BasicUserInfo {
    private int userID;
    private String userName;
    private String iconURL;
    private String email;
    private Date registrationTime;
    private int isPost;
    private int isDelete;
    private int isMember;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public int getIsPost() {
        return isPost;
    }

    public void setIsPost(int isPost) {
        this.isPost = isPost;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    @Override
    public String toString() {
        return "BasicUserInfo{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", email='" + email + '\'' +
                ", registrationTime=" + registrationTime +
                ", isPost=" + isPost +
                ", isDelete=" + isDelete +
                ", isMember=" + isMember +
                '}';
    }
}
