package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.util.Constants;

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

    @Override
    public void getMeals(Subscriber<MealsDTO> mealsDTOSubscriber) {
        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        Log.d("CONTROLLER", "getMeals");

        // Create a call instance for meals.
        Observable<MealsDTO> observable = mealAPIService.getMeals(Constants.TOKEN_VALUE, Constants.USER_VALUE);

        observable.subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(mealsDTOSubscriber);
    }
}
