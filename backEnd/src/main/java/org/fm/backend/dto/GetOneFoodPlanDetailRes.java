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
public class GetOneFoodPlanDetailRes {
    private int foodPlanID;
    private Date date;
    private int mealType;
    private boolean state;
    private int numOfTypes;
    private List<FoodItemDTO> foods;
}
