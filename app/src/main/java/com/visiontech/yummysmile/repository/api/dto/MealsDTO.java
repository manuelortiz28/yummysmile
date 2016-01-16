package com.visiontech.yummysmile.repository.api.dto;

import java.util.Collections;
import java.util.List;

/**
 * Class to handle the response of Meals.
 *
 * @author hector.torres
 */
public class MealsDTO {
    private List<MealDTO> meals = Collections.emptyList();

    public List<MealDTO> getMeals() {
        return meals;
    }
}
