package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.google.common.collect.FluentIterable;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.models.transformers.MealTransform;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.repository.api.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

import java.util.List;

import javax.inject.Inject;

/**
 * Class to implement the scope for this controller.
 *
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {
    private static final String LOG_TAG = MealsControllerImpl.class.getName();

    private final AuthenticationController authenticationController;
    private final MealAPIService mealAPIService;

    @Inject
    public MealsControllerImpl(MealAPIService mealAPIService, AuthenticationControllerImpl authenticationController) {
        this.mealAPIService = mealAPIService;
        this.authenticationController = authenticationController;
    }

    @Override
    public void getMeals(final ResultListener<MealsControllerImpl.MealsResponse> resultListener) {
        Log.d(LOG_TAG, "getMeals()");

        final User userLoggedIn = authenticationController.getUserLoggedIn();

        String token = "";
        String userId = "";

        if (userLoggedIn != null) {
            token = userLoggedIn.getToken();
            userId = userLoggedIn.getId();
        }

        final MealsResponse mealsResponse = new MealsResponse();

        FactoryRestAdapter.invokeService(
                mealAPIService.getMeals(token, userId),
                new BaseSubscriber<MealsDTO>(resultListener, mealsResponse) {
                    @Override
                    protected void onSuccess(MealsDTO serviceResponse) {

                        List<Meal> mealList =
                                FluentIterable
                                        .from(serviceResponse.getMeals())
                                        .transform(MealTransform.getTransformMealDtoToMeal())
                                        .toList();

                        mealsResponse.setPayload(mealList);
                    }
                }
        );
    }

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    public static class MealsResponse extends BaseResponse<List<Meal>> {
        private String otroAttr;
    }
}
