package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.Meal;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.controller.AuthenticationControllerImpl;
import com.visiontech.yummysmile.ui.controller.MealsControllerImpl;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

import java.util.List;

import javax.inject.Inject;

/**
 * Class that is the intermediary between the view and model based on MVP Pattern.
 *
 * @author hetorres
 */
public class MainPresenter extends BasePresenter {

    private final MainView mainView;
    private final MealsControllerImpl mealsController;
    private final AuthenticationController authenticationController;

    @Inject
    public MainPresenter(MainView mainView, MealsControllerImpl mealsController, AuthenticationControllerImpl authenticationController) {
        this.mainView = mainView;
        this.mealsController = mealsController;
        this.authenticationController = authenticationController;
    }

    public void fetchMeals() {
        mainView.showProgress(true);
        mealsController.getMeals(new ResultListener<MealsControllerImpl.MealsResponse>() {
            @Override
            public void onResult(MealsControllerImpl.MealsResponse result) {
                mainView.showProgress(false);
                if (result.isSuccess()) {

                    List<Meal> mealList = result.getPayload();
                    if (!mealList.isEmpty()) {
                        mainView.showMealsItems(mealList);
                    } else {
                        //TODO show some empty view here instead a toast.
                        mainView.showMessage("All good, but no meals");
                    }
                } else {
                    mainView.setRefreshing(false);

                    //FIXME We could create a better handling error, including a generic one
                    if (result.getError().getCode() == HttpResponseCode.UNAUTHORIZED) {
                        mainView.showLoginScreen();
                    } else {
                        //FIXME find the final copy.
                        //TODO Do we have to show other view? like some text on the layout?
                        mainView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                    }
                }
            }
        });
    }

    //FIXME This method could be in a BasePresenter
    /**
     * Log outs the current user and handle the response
     */
    public void logoutUser() {
        authenticationController.logOutUser(
                new ResultListener<AuthenticationControllerImpl.LoggedOutResponse>() {
                    @Override
                    public void onResult(AuthenticationControllerImpl.LoggedOutResponse result) {
                        if (result.isSuccess()) {
                            mainView.showLoginScreen();
                        } else {

                            //FIXME find the final copy.
                            mainView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                        }
                    }
                }
        );
    }

    //FIXME This method could be in a BasePresenter
    /**
     * Validate if the user is logged in, and call to the properly view methods
     */
    public void validateUserLoggedIn() {
        User user = authenticationController.getUserLoggedIn();
        if (user == null) {
            mainView.showLoginScreen();
        } else {
            mainView.showUserInfo(user);
        }
    }
}
