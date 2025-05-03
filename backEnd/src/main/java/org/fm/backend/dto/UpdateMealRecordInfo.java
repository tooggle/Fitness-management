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
public class UpdateMealRecordInfo {
    private int recordID;
    private Date mealTime;
    private String mealPhoto;
    private List<FoodItemDTO> foods;
}
