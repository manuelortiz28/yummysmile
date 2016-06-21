package com.visiontech.yummysmile.ui.presenter;

import android.support.annotation.Nullable;
import android.util.Log;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.repository.api.response.ErrorResponse;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateMealFragmentView;

import java.io.File;

import javax.inject.Inject;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class CreateMealPresenter extends BasePresenter {
    private static final String LOG_TAG = CreateMealPresenter.class.getName();
    private final MealsController mealsController;
    private final BaseFragmentView baseFragmentView;
    private final CreateMealFragmentView createMealFragmentView;

    @Inject
    public CreateMealPresenter(
            YummySmileApplication application,
            MealsController mealsController,
            @Nullable BaseFragmentView baseFragmentView,
            @Nullable CreateMealFragmentView createMealFragmentView) {

        super(application);

        this.mealsController = mealsController;
        this.baseFragmentView = baseFragmentView;
        this.createMealFragmentView = createMealFragmentView;
    }

    public void createMeal(String mealName, File photo) {
        Log.d(LOG_TAG, "createMealPresenter()");
        baseFragmentView.showProgress(true);

        Meal meal = new Meal();
        meal.setName(mealName);

        mealsController.createMeal(meal, photo, new ResultListener<MealsController.CreateMealResponse>() {
            @Override
            public void onResult(MealsController.CreateMealResponse result) {
                baseFragmentView.showProgress(false);
                if (result.isSuccess()) {
                    createMealFragmentView.createMealResponse(result.getPayload());
                } else {
                    //TODO Do we have to show other view? like some text on the layout?
                    ErrorResponse error = result.getError();
                    baseFragmentView.showMessage(String.format(getContext().getString(R.string.general_error), error.getMessage()));
                    Log.e(LOG_TAG, "Message: " + error.getMessage());
                    Log.e(LOG_TAG, "Code: " + error.getCode());
                }
            }
        });
    }
}
