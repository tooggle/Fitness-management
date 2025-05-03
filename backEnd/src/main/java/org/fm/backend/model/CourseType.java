package org.fm.backend.model;

public class CourseType {
    private int typeID;
    private String typeName;

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public String toString() {
        return "CourseType{" +
                "typeID=" + typeID +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
