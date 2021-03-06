package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.HomeFragmentView;

import java.util.List;

import javax.inject.Inject;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class HomePresenter {

    private final MealsController mealsController;
    private final BaseFragmentView baseFragmentView;
    private final HomeFragmentView homeView;
    private final BaseActivityView baseActivityView;
    private final Context context;

    @Inject
    public HomePresenter(
            Context context,
            MealsController mealsController,
            @Nullable BaseActivityView baseActivityView,
            @Nullable BaseFragmentView baseFragmentView,
            @Nullable HomeFragmentView homeView) {

        this.context = context;
        this.mealsController = mealsController;
        this.baseActivityView = baseActivityView;
        this.baseFragmentView = baseFragmentView;
        this.homeView = homeView;
    }

    public void fetchMeals() {
        baseFragmentView.showProgress(true);
        mealsController.getMeals(new ResultListener<MealsController.MealsResponse>() {
            @Override
            public void onResult(MealsController.MealsResponse result) {
                baseFragmentView.showProgress(false);
                if (result.isSuccess()) {

                    List<Meal> mealList = result.getPayload();
                    if (!mealList.isEmpty()) {
                        homeView.showMealsItems(mealList);
                    } else {
                        //TODO show some empty view here instead a toast.
                        baseFragmentView.showMessage("All good, but no meals");
                    }
                } else {
                    homeView.setRefreshing(false);

                    //FIXME We could create a better handling error, including a generic one
                    if (HttpResponseCode.UNAUTHORIZED == result.getError().getCode()) {
                        baseActivityView.showLoginScreen();
                    } else {
                        //FIXME find the final copy.
                        //TODO Do we have to show other view? like some text on the layout?
                        baseFragmentView.showMessage(context.getString(R.string.general_error, result.getError().getMessage()));
                    }
                }
            }
        });
    }
}
