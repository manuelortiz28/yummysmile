package com.visiontech.yummysmile.ui.controller;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.squareup.otto.Bus;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.MealAPIService;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.events.BaseResponseEvent;
import com.visiontech.yummysmile.util.Constants;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action1;

/**
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {

    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());

    Bus eventBus;

    public MealsControllerImpl(Bus eventBus){
        this.eventBus = eventBus;
    }

    /**
     * ==================================================================================================
     *                                         Implementations
     * ==================================================================================================
     */

    @Override
    public void getMeals() {
        final RetrieveMealsEvent mealsEvent = new RetrieveMealsEvent();

        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        Log.i("CONTROLLER", "getMeals");

        // Create a call instance for meals.
        Observable<MealsDTO> observable = mealAPIService.getMeals(Constants.TOKEN_VALUE, Constants.USER_VALUE);
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribeOn(HandlerScheduler.from(MAIN_THREAD));
        observable.subscribe(new Subscriber<MealsDTO>() {
            @Override
            public void onCompleted() {
                Log.i("CONTROLLER", "onCompleted");
            }

            @Override
            public void onError(Throwable e) {
                Log.i("CONTROLLER", "onError");
            }

            @Override
            public void onNext(MealsDTO mealsDTO) {
                Log.i("CONTROLLER", "onNext");
                mealsEvent.setResult(mealsDTO);

                MAIN_THREAD.post(new Runnable() {
                    @Override
                    public void run() {
                        eventBus.post(mealsEvent);
                    }
                });
            }
        });

    }

    /**
     * ==================================================================================================
     *                                         private Methods
     * ==================================================================================================
     */

    /**
     * ==================================================================================================
     *                                         Events
     * ==================================================================================================
     */

    public class RetrieveMealsEvent extends BaseResponseEvent<MealsDTO> {

    }

}
