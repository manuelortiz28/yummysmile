package com.visiontech.yummysmile.ui.controller;

import com.visiontech.yummysmile.repository.api.dto.MealsDTO;

import rx.Subscriber;

/**
 * Interface to define the scope for this controller.
 * @author hector.torres
 */
public interface MealsController {
    /**
     * Method that gets meals.
     * @param mealsDTOSubscriber subscriber that we want to return.
     */
    void getMeals(Subscriber<MealsDTO> mealsDTOSubscriber);
}
