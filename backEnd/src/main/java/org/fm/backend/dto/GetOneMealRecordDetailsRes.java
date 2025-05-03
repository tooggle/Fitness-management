package org.fm.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetOneMealRecordDetailsRes {
    private int recordID;
    private Date mealTime;
    private String mealPhoto;
    private Date createdTime;
    private int totalCalorie;
    private String diningAdvice;
    private List<FoodRecord> foods;
}
