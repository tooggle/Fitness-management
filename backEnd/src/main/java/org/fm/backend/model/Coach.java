package org.fm.backend.model;

public class Coach {
    private int coachID ;

    //教练的用户名称
    private String userName ;

    private int age ;

    private String gender ;

    private String iconURL ;

    private int isMember ;

    //教练的真实名称
    private String coachName ;

    private String instructorHonors ;

    public String getInstructorHonors() {
        return instructorHonors;
    }

    public void setInstructorHonors(String instructorHonors) {
        this.instructorHonors = instructorHonors;
    }

    public String getCoachName() {
        return coachName;
    }

    public void setCoachName(String coachName) {
        this.coachName = coachName;
    }

    public int getIsMember() {
        return isMember;
    }

    public void setIsMember(int isMember) {
        this.isMember = isMember;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getCoachID() {
        return coachID;
    }

    public void setCoachID(int coachID) {
        this.coachID = coachID;
    }

    @Override
    public String toString() {
        return "Coach{" +
                "coachID=" + coachID +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", isMember=" + isMember +
                ", coachName='" + coachName + '\'' +
                ", instructorHonors='" + instructorHonors + '\'' +
                '}';
    }
}
