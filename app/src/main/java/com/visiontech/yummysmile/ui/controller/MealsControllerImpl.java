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
import rx.schedulers.Schedulers;

/**
 * @author hector.torres
 */
public class MealsControllerImpl implements MealsController {

    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());
    Bus eventBus;

    public MealsControllerImpl(Bus eventBus) {
        this.eventBus = eventBus;
    }

    /**
     * ==================================================================================================
     * Implementations
     * ==================================================================================================
     */

    @Override
    public void getMeals() {
        // Create an instance of our API interface.
        MealAPIService mealAPIService = FactoryRestAdapter.createRetrofitService(MealAPIService.class);

        Log.i("CONTROLLER", "getMeals");

        // Create a call instance for meals.
        Observable<MealsDTO> observable = mealAPIService.getMeals(Constants.TOKEN_VALUE, Constants.USER_VALUE);
        observable.observeOn(AndroidSchedulers.mainThread());
        observable.subscribeOn(Schedulers.newThread());

        observable.subscribe(new MealsSubscriber());
    }

    /**
     * ==================================================================================================
     *                                         private Methods
     * ==================================================================================================
     */

    /**
     * ==================================================================================================
     * Events
     * ==================================================================================================
     */

    public class RetrieveMealsEvent extends BaseResponseEvent<MealsDTO> {

    }

    class MealsSubscriber extends Subscriber<MealsDTO> {
        RetrieveMealsEvent mealsEvent = new RetrieveMealsEvent();

        @Override
        public void onCompleted() {
            Log.i("CONTROLLER", "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.i("CONTROLLER", "onError");
            mealsEvent.setResult(e);
        }

        @Override
        public void onNext(MealsDTO response) {
            Log.i("CONTROLLER", "onNext");
            mealsEvent.setResult(response);
            try {
                Log.i("CONTROLLER", "before Sleep");
                Thread.sleep(10000);
                Log.i("CONTROLLER", "after Sleep");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.i("CONTROLLER", "Despierto");
            if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                Log.i("CONTROLLER", "MISMO HILO 1");
            } else {
                Log.i("CONTROLLER", "OTRO HILO 1");
            }
            MAIN_THREAD.post(new Runnable() {
                @Override
                public void run() {
                    if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
                        Log.i("CONTROLLER", "MISMO HILO 2");
                    } else {
                        Log.i("CONTROLLER", "OTRO HILO 2");
                    }
                    eventBus.post(mealsEvent);
                }
            });
        }
    }

}
