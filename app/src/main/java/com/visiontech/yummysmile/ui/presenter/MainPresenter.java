package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

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
        mealsController = new MealsControllerImpl(this);
    }

    public void fetchMeals() {
        mainView.showProgress(true);
        mealsController.getMeals();
    }

    public <T> void onNext(T data) {
        MealsDTO mealsDTO = (MealsDTO) data;
        mainView.showProgress(false);
        if (mealsDTO != null && !mealsDTO.getMeals().isEmpty()) {
            mainView.mealsItems(mealsDTO);
        } else {
            mainView.showMessage("All good no results");
        }
    }

    public void onError(Throwable e) {
        mainView.showMessage("There is something wrong: " + e.getMessage());
        //TODO Do we have to show other view? like some text on the layout?
    }
}
