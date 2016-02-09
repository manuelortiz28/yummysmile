package com.visiontech.yummysmile.ui.controller;

import android.util.Log;

import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.subscriber.BaseResponse;
import com.visiontech.yummysmile.ui.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.ui.subscriber.ResultListener;
import com.visiontech.yummysmile.util.Constants;

import javax.inject.Inject;

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

    private final MealAPIService mealAPIService;

    @Inject
    public MealsControllerImpl(MealAPIService mealAPIService) {
        this.mealAPIService = mealAPIService;
    }

    @Override
    public void getMeals(ResultListener result) {
        Log.d(LOG_TAG, "getMeals()");

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

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    public static class MealsResponse extends BaseResponse<MealsDTO> {
    }
}
