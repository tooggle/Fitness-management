package org.fm.backend.dto;


public class ExpandUserInfo {
    private int userID;
    private String userName;
    private String iconURL;
    private int age;
    private String gender;
    private String tags;
    private String introduction;
    private int isMember;
    private String goalType;
    private float goalWeight;

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

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
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

    @Override
    public String toString() {
        return "ExpandUserInfo{" +
                "userID=" + userID +
                ", userName='" + userName + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", tags='" + tags + '\'' +
                ", introduction='" + introduction + '\'' +
                ", isMember=" + isMember +
                ", goalType='" + goalType + '\'' +
                ", goalWeight=" + goalWeight +
                '}';
    }
}
