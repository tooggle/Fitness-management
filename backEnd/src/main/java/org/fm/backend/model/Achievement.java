package org.fm.backend.model;

public class Achievement {
    private int AchievementID ;
    private String Name ;
    private int Target ;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAchievementID() {
        return AchievementID;
    }

    public void setAchievementID(int achievementID) {
        AchievementID = achievementID;
    }

    public int getTarget() {
        return Target;
    }

    public void setTarget(int target) {
        Target = target;
    }

    @Override
    public String toString() {
        return "Achievement{" +
                "AchievementID=" + AchievementID +
                ", Name='" + Name + '\'' +
                ", Target=" + Target +
                '}';
    }
}
