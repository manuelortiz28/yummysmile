package com.visiontech.yummysmile.ui.controller;

import com.google.gson.JsonObject;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

import java.io.File;
import java.util.List;

/**
 * Interface to define the scope for this controller.
 *
 * @author hector.torres
 */
public interface MealsController {

    /**
     * Method that gets meals list of the user logged in
     */
    void getMeals(ResultListener<MealsResponse> result);

    /**
     * Method that upload a new meal
     * @param meal   meal object that we want to create
     * @param photo  picture of the meal
     * @param result listener to return the response.
     */
    //FIXME Use a DTO instead of JsonObject
    void createMeal(JsonObject meal, File photo, ResultListener result);

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    class MealsResponse extends BaseResponse<List<Meal>> {
    }

    class CreateMealResponse extends BaseResponse<JsonObject> {
    }
}
