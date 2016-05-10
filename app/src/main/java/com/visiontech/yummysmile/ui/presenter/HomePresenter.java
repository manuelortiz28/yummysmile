package com.visiontech.yummysmile.ui.presenter;

import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.YummySmileApplication;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.MealsController;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.HomeFragmentView;

import java.util.List;

import javax.inject.Inject;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class HomePresenter extends BasePresenter {

    private final MealsController mealsController;
    private final BaseFragmentView baseFragmentView;
    private final HomeFragmentView homeView;
    private final DrawerActivityView drawerView;

    @Inject
    public HomePresenter(
            YummySmileApplication application,
            MealsControllerImpl mealsController,
            @Nullable BaseFragmentView baseFragmentView,
            @Nullable HomeFragmentView homeView,
            @Nullable DrawerActivityView drawerView) {

        super(application);

        this.mealsController = mealsController;
        this.baseFragmentView = baseFragmentView;
        this.homeView = homeView;
        this.drawerView = drawerView;
    }

    public void fetchMeals() {
        baseFragmentView.showProgress(true);
        mealsController.getMeals(new ResultListener<MealsControllerImpl.MealsResponse>() {
            @Override
            public void onResult(MealsControllerImpl.MealsResponse result) {
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
                        drawerView.showLoginScreen();
                    } else {
                        //FIXME find the final copy.
                        //TODO Do we have to show other view? like some text on the layout?
                        baseFragmentView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                    }
                }
            }
        });
    }
}
