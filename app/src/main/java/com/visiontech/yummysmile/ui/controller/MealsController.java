package com.visiontech.yummysmile.ui.controller;

import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

/**
 * Interface to define the scope for this controller.
 *
 * @author hector.torres
 */
public interface MealsController {
    /**
     * Method that gets meals list of the user logged in
     */
    void getMeals(ResultListener<MealsControllerImpl.MealsResponse> result);
}
