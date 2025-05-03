package org.fm.backend.model;

public class FitnessEquiOperation {
    private String equipmentName;
    private String imgUrl ;
    private String briefIntr ;
    private String equipmentGuide;
    private String createTime;
    private String lastUpdateTime;
    private float userRatings;//用户评分，选填

    public String getEquipmentGuide() {
        return equipmentGuide;
    }

    public void setEquipmentGuide(String equipmentGuide) {
        this.equipmentGuide = equipmentGuide;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBriefIntr() {
        return briefIntr;
    }

    public void setBriefIntr(String briefIntr) {
        this.briefIntr = briefIntr;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public float getUserRatings() {
        return userRatings;
    }

    public void setUserRatings(float userRatings) {
        this.userRatings = userRatings;
    }

    @Override
    public String toString() {
        return "FitnessEquiOperation{" +
                "equipmentName='" + equipmentName + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", briefIntr='" + briefIntr + '\'' +
                ", equipmentGuide='" + equipmentGuide + '\'' +
                ", createTime='" + createTime + '\'' +
                ", lastUpdateTime='" + lastUpdateTime + '\'' +
                ", userRatings=" + userRatings +
                '}';
    }
}
