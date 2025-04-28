package org.fm.backend.model;

import java.util.Date;

public class User {
    private int userID;
    private String userName;
    private String password;
    private String email;
    private Date registrationTime;
    private String role;
    private String iconURL ;
    private int age ;
    private String gender ;
    private String tags ;
    private String introduction ;
    private String goalType ;
    private float goalWeight ;
    private Date lastLoginTime;
    private int vigorTokenBalance;
    private int isMember ;
    private int isPost ;
    private int isDelete ;

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getRegistrationTime() {
        return registrationTime;
    }

    public void setRegistrationTime(Date registrationTime) {
        this.registrationTime = registrationTime;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getGoalType() {
        return goalType;
    }

    public void setGoalType(String goalType) {
        this.goalType = goalType;
    }

    public float getGoalWeight() {
        return goalWeight;
    }

    public void setGoalWeight(float goalWeight) {
        this.goalWeight = goalWeight;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public int getVigorTokenBalance() {
        return vigorTokenBalance;
    }

    public void setVigorTokenBalance(int vigorTokenBalance) {
        this.vigorTokenBalance = vigorTokenBalance;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
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

    @Override
    public String toString() {
        return "User{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", registrationTime=" + registrationTime +
                ", role='" + role + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", tags='" + tags + '\'' +
                ", introduction='" + introduction + '\'' +
                ", goalType='" + goalType + '\'' +
                ", goalWeight=" + goalWeight +
                ", lastLoginTime=" + lastLoginTime +
                ", vigorTokenBalance=" + vigorTokenBalance +
                ", isMember=" + isMember +
                ", isPost=" + isPost +
                ", isDelete=" + isDelete +
                '}';
    }
}
