package com.visiontech.yummysmile.ui.controller;

import com.google.gson.JsonObject;
import com.visiontech.yummysmile.ui.subscriber.ResultListener;

import java.io.File;

/**
 * Interface to define the scope for this controller.
 *
 * @author hector.torres
 */
public interface MealsController {
    /**
     * Method that gets meals.
     * @param result listener to return the response.
     */
    void getMeals(ResultListener result);

    /**
     * Method that upload a new meal
     *
     * @param meal  meal object that we want to create
     * @param photo picture of the meal
     */
    void createMeal(JsonObject meal, File photo);

}
