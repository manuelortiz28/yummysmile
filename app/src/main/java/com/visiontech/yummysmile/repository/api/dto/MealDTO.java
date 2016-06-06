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

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
