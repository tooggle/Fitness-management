package org.fm.backend.dto;

import lombok.Data;

@Data
public class TokenValidationResult {
    public String Role ;
    public int userID ;
    public boolean IsValid ;

    public TokenValidationResult(int userID, String Role, boolean IsValid) {
        this.userID = userID;
        this.Role = Role;
        this.IsValid = IsValid;
    }

    @Override
    public String toString() {
        return "TokenValidationResult{" +
                "Role='" + Role + '\'' +
                ", userID=" + userID +
                ", IsValid=" + IsValid +
                '}';
    }
}
