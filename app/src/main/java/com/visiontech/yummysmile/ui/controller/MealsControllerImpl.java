package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.google.gson.JsonObject;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.presenter.BasePresenter;
import com.visiontech.yummysmile.util.Constants;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Class to implement the scope for this controller.
 *
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {
    private static final String LOG_TAG = MealsControllerImpl.class.getName();
    private BasePresenter presenter;

    public MealsControllerImpl(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getMeals() {
        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        Log.d(LOG_TAG, "getMeals()");

        // Create a call instance for meals.
        Observable<MealsDTO> observable = mealAPIService.getMeals(Constants.TOKEN_VALUE, Constants.USER_VALUE);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<MealsDTO>() {
                      @Override
                      public void onCompleted() {
                          Log.d(LOG_TAG, "onCompleted()");
                      }

                      @Override
                      public void onError(Throwable e) {
                          Log.d(LOG_TAG, "onError()");
                          presenter.onError(e);
                      }

                      @Override
                      public void onNext(MealsDTO mealsDTO) {
                          Log.d(LOG_TAG, "onNext()");
                          presenter.onNext(mealsDTO);
                      }
                  });
    }

    @Override
    public void createMeal(JsonObject meal, File photo) {
        Log.d(LOG_TAG, "createMeal()");

        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        // Creating the request body base on the file
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), photo);

        //Making the request
        Observable<JsonObject> observable = mealAPIService.createMeal(Constants.TOKEN_VALUE, Constants.USER_VALUE, requestBody, meal);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(new Subscriber<JsonObject>() {
                      @Override
                      public void onCompleted() {
                          Log.d(LOG_TAG, "onCompleted()");
                      }

                      @Override
                      public void onError(Throwable e) {
                          Log.d(LOG_TAG, "onError()");
                          presenter.onError(e);
                      }

                      @Override
                      public void onNext(JsonObject response) {
                          Log.d(LOG_TAG, "onNext()");
                          presenter.onNext(response);
                      }
                  });
    }
}
