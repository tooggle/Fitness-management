package org.fm.backend.dto;

import lombok.Data;
import java.util.List;

@Data
public class UpdateFoodPlanInfo {
    private int foodPlanID;
    private int mealType;
    private List<FoodItemDTO> foods;
}
