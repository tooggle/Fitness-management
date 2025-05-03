package org.fm.backend.model;

import lombok.Data;

import java.util.Date;

@Data
public class SingleRecipe {
    private int recipeID;
    private String title;
    private String imgUrl;
    private String content;
    private Date releaseTime;
}
