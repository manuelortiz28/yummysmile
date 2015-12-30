package com.visiontech.yummysmile.repository.api.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Class to handle the information of each Meal.
 *
 * @author hector.torres
 */
public class MealDTO {
    @SerializedName("objectId")
    private String id;
    private String name;
    @SerializedName("filename") //Fixme on service side.
    private String fileName;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getFileName() {
        return fileName;
    }
}
