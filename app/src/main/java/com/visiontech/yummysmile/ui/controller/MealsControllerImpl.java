package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.google.common.collect.FluentIterable;
import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.models.transformers.MealTransform;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.repository.api.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.util.Constants;

import java.io.File;
import java.util.List;

import javax.inject.Inject;

/**
 * Class to implement the scope for this controller.
 *
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {
    private static final String LOG_TAG = MealsControllerImpl.class.getName();

    private String token = "";
    private String userId = "";

    protected final AuthenticationController authenticationController;
    protected final MealAPIService mealAPIService;

    @Inject
    public MealsControllerImpl(MealAPIService mealAPIService, AuthenticationControllerImpl authenticationController) {
        this.mealAPIService = mealAPIService;
        this.authenticationController = authenticationController;
    }

    @Override
    public void getMeals(final ResultListener<MealsControllerImpl.MealsResponse> resultListener) {
        Log.d(LOG_TAG, "getMeals()");

        obtainUserInfo();
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

    @Override
    public void createMeal(JsonObject meal, File photo, ResultListener result) {
        Log.d(LOG_TAG, "createMeal()");

        obtainUserInfo();
        final CreateMealResponse createMealResponse = new CreateMealResponse();

        // Creating the request body base on the file
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.API_MULTIPART_DATA), photo);

        FactoryRestAdapter.invokeService(
                mealAPIService.createMeal(token, userId, requestBody, meal),
                new BaseSubscriber<JsonObject>(result, createMealResponse) {

                    @Override
                    protected void onSuccess(JsonObject serviceResponse) {
                        if (serviceResponse.has("objectId")) {
                            Log.d(LOG_TAG, "Success!");
                            createMealResponse.setPayload(serviceResponse);
                        }
                    }
                });
    }

    private void obtainUserInfo() {
        final User userLoggedIn = authenticationController.getUserLoggedIn();

        if (userLoggedIn != null) {
            token = userLoggedIn.getToken();
            userId = userLoggedIn.getId();
        }
    }

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    public static class MealsResponse extends BaseResponse<List<Meal>> {
    }

    public static class CreateMealResponse extends BaseResponse<JsonObject> {
    }
}
