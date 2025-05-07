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
public class AISummaryRes {
    private int recordID;
    private Date mealTime;
    private List<FoodRecord> foods;


    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FoodRecord {
        private String foodName;
        private int foodAmount;

    }
}