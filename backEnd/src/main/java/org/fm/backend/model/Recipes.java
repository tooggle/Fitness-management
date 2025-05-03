package org.fm.backend.model;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class Recipes {
    private List<SingleRecipe> recipes;
}
