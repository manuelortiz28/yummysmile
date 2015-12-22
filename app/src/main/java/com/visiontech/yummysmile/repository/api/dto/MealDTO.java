package com.visiontech.yummysmile.repository.api.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class to handle the information of each Meal.
 * @author hector.torres
 */
public class MealDTO {
    @SerializedName("objectId")
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
