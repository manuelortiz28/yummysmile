package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.subscriber.BaseResponse;
import com.visiontech.yummysmile.ui.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.ui.subscriber.ResultListener;
import com.visiontech.yummysmile.util.Constants;

import java.io.File;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Class to implement the scope for this controller.
 *
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {
    private static final String LOG_TAG = MealsControllerImpl.class.getName();

    @Override
    public void getMeals(ResultListener result) {
        Log.d(LOG_TAG, "getMeals()");
        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        // Create a call instance for meals.
        Observable<MealsDTO> observable = mealAPIService.getMeals(Constants.TOKEN_VALUE, Constants.USER_VALUE);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new BaseSubscriber<MealsDTO>(result) {
                      @Override
                      public BaseResponse getBaseResponse() {
                          return new MealsResponse();
                      }
                  });
    }

    @Override
    public void createMeal(JsonObject meal, File photo, ResultListener result) {
        Log.d(LOG_TAG, "createMeal()");

        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        // Creating the request body base on the file
        RequestBody requestBody = RequestBody.create(MediaType.parse(Constants.API_MULTIPART_DATA), photo);

        //Making the request
        Observable<JsonObject> observable = mealAPIService.createMeal(Constants.TOKEN_VALUE, Constants.USER_VALUE, requestBody, meal);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new BaseSubscriber<JsonObject>(result) {
                      @Override
                      public BaseResponse getBaseResponse() {
                          return new CreateMealResponse();
                      }
                  });
    }

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    public static class MealsResponse extends BaseResponse<MealsDTO> {
    }

    public static class CreateMealResponse extends BaseResponse<JsonObject> {
    }
}
