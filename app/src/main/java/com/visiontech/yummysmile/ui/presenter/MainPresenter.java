package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class MainPresenter extends BasePresenter {
    private MainView mainView;
    private MealsControllerImpl mealsController;
    private Context context;

    public MainPresenter(MainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        mealsController = new MealsControllerImpl(this);
    }

    public void fetchMeals() {
        mainView.showProgress(true);
        mealsController.getMeals();
    }

    public <T> void onNext(T data) {
        MealsDTO mealsDTO = (MealsDTO) data;
        if (mealsDTO != null && !mealsDTO.getMeals().isEmpty()) {
            mainView.mealsItems(mealsDTO);
        } else {
            mainView.showProgress(false);
            //TODO show some empty view here instead a toast.
            mainView.showMessage("All good, but no meals");
        }
    }

    public void onError(Throwable e) {
        //FIXME find the final copy.
        mainView.showMessage(String.format(context.getString(R.string.general_error), e.getMessage()));
        //TODO Do we have to show other view? like some text on the layout?
        mainView.showProgress(false);
    }
}
