package com.visiontech.yummysmile.ui.controller;

import com.google.gson.JsonObject;

import java.io.File;

/**
 * Interface to define the scope for this controller.
 *
 * @author hector.torres
 */
public interface MealsController {
    /**
     * Method that gets meals.
     */
    void getMeals();

    /**
     * Method that upload a new meal
     * @param meal meal object that we want to create
     * @param photo picture of the meal
     */
    void createMeal(JsonObject meal, File photo);
}
