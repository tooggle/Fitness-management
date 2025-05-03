package org.fm.backend.model;

public class Trainee {
    private int traineeID;
    private String userName;
    private int Age;
    private String Gender;
    private String iconURL;

    private String traineeName;

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public int getTraineeTD() {
        return traineeID;
    }

    public void setTraineeTD(int traineeTD) {
        this.traineeID = traineeTD;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getIconURL() {
        return iconURL;
    }

    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }

    public String getTraineeName() {
        return traineeName;
    }

    public void setTraineeName(String traineeName) {
        this.traineeName = traineeName;
    }

    @Override
    public String toString() {
        return "Trainee{" +
                "traineeID=" + traineeID +
                ", userName='" + userName + '\'' +
                ", Age=" + Age +
                ", Gender='" + Gender + '\'' +
                ", iconURL='" + iconURL + '\'' +
                ", traineeName='" + traineeName + '\'' +
                '}';
    }
}
