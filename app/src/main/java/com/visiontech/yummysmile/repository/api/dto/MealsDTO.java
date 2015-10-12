package com.visiontech.yummysmile.repository.api.dto;

import com.google.common.collect.Lists;

import java.util.Collections;
import java.util.List;

/**
 * Class to handle the response of Meals.
 * @author hector.torres
 */
public class MealsDTO {
    List<MealDTO> meals = Collections.emptyList();

    public List<MealDTO> getMeals(){
        return meals;
    }
}
