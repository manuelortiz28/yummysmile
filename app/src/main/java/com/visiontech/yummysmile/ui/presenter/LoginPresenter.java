package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.controller.AuthenticationControllerImpl;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.LoginActivityView;

import javax.inject.Inject;

/**
 * @author manuel ortiz
 *
 * Class in the Presenter tier from the MVP pattern, containing methods related to the user login
 *
 */
public class LoginPresenter {
    private final Context context;
    private final AuthenticationController authenticationController;
    private final DrawerActivityView drawerView;
    private final LoginActivityView loginView;

    @Inject
    public LoginPresenter(
            Context context,
            AuthenticationControllerImpl authenticationController,
            @Nullable LoginActivityView loginView,
            @Nullable DrawerActivityView drawerView) {

        this.loginView = loginView;
        this.authenticationController = authenticationController;
        this.context = context;
        this.drawerView = drawerView;
    }

    /**
     * Perform the authentication of the user, and handle the success response, or handle any error related
     * @param username
     * @param password
     */
    public void authenticate(String username, String password) {
        authenticationController.login(username, password, new ResultListener<AuthenticationControllerImpl.LoggedInResponse>() {

            @Override
            public void onResult(AuthenticationControllerImpl.LoggedInResponse result) {
                if (result.isSuccess()) {
                    loginView.showSuccess(result.getPayload());
                } else {

                    if (HttpResponseCode.UNAUTHORIZED == result.getError().getCode()
                            && !result.getError().getErrors().isEmpty()) {
                        loginView.showError(result.getError().getErrors().get(0).getMessage());
                    } else {
                        loginView.showError(
                                String.format(
                                        context.getString(R.string.general_error), result.getError().getMessage()));
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
                            drawerView.showLoginScreen();
                        } /*else {
                            //FIXME find the final copy.
                            commonView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                        }*/
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
            drawerView.showLoginScreen();
        } else {
            drawerView.showUserInfo(user);
        }
    }
}
