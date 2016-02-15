package com.visiontech.yummysmile.models.transformers;

import com.google.common.base.Function;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;

/**
 * @author manuel.ortiz
 *
 * Transformer class to convert between different types of objects related to Meals
 *
 */
public final class MealTransform {

    private MealTransform() {
    }

    private static Function<MealDTO, Meal> transformMealDtoToMeal = new Function<MealDTO, Meal>() {
        public Meal apply(MealDTO mealDTO) {

            if (mealDTO == null) {
                return null;
            }

            Meal newMeal = new Meal();
            newMeal.setId(mealDTO.getId());
            newMeal.setName(mealDTO.getName());
            newMeal.setFileName(mealDTO.getFileName());

            return newMeal;
        }
    };

    private static Function<Meal, MealDTO> transformMealToMealDto = new Function<Meal, MealDTO>() {
        @Override
        public MealDTO apply(Meal meal) {
            if (meal == null) {
                return null;
            }

            MealDTO newMealDTO = new MealDTO();
            newMealDTO.setId(meal.getId());
            newMealDTO.setName(meal.getName());
            newMealDTO.setFileName(meal.getFileName());

            return newMealDTO;
        }
    };

    /**
     * Function to transform a MealDto object into a Meal
     */
    public static Function<MealDTO, Meal> getTransformMealDtoToMeal() {
        return transformMealDtoToMeal;
    }

    /**
     * Function to transform a Meal object into a MealDto
     */
    public static Function<Meal, MealDTO> getTransformMealToMealDto() {
        return transformMealToMealDto;
    }
}
