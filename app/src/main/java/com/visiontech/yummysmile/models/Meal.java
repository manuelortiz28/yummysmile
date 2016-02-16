package com.visiontech.yummysmile.models;

/**
 * @autor manuel.ortiz
 *
 * Meal model containing all the information necessary for a meal
 *
 */
public class Meal {
    private String id;
    private String name;
    private String fileName;

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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
