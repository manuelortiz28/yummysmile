package com.visiontech.yummysmile.ui.presenter;

import android.util.Log;

import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.ui.subscriber.BaseSubscriber;

/**
 * Class that is the intermediary between the view and model
 *
 * @author hetorres
 */
public class MainPresenter extends BasePresenter {
    private MainView mainView;
    private MealsControllerImpl mealsController;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
        mealsController = new MealsControllerImpl();
    }

    public void fetchMeals() {
        mainView.showProgress(true);
        mealsController.getMeals(new BaseSubscriber(this));
    }

    public <T> void onNext(T data) {
        MealsDTO mealsDTO = (MealsDTO) data;
        mainView.showProgress(false);
        if (mealsDTO != null && !mealsDTO.getMeals().isEmpty()) {
            for (MealDTO meal : mealsDTO.getMeals()) {
                Log.d("", meal.getName());
            }
            mainView.showMessage("All good");
        } else {
            mainView.showMessage("All good no results");
        }
    }

    public void onError(Throwable e) {
        mainView.showMessage("There is something wrong: " + e.getMessage());
    }
}
