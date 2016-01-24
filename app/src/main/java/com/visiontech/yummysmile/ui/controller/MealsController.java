package com.visiontech.yummysmile.ui.controller;

import com.visiontech.yummysmile.ui.subscriber.ResultListener;

/**
 * Interface to define the scope for this controller.
 *
 * @author hector.torres
 */
public interface MealsController {
    /**
     * Method that gets meals.
     */
    void getMeals(ResultListener result);
}
