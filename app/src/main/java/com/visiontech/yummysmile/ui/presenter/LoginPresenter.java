package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.models.YummySharedPreferences;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.UserSessionView;

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
    private final YummySharedPreferences sharedPreferences;

    private final BaseActivityView baseActivityView;
    private final AuthenticatorActivityView loginView;
    private final UserSessionView userSessionView;

    @Inject
    public LoginPresenter(
            Context context,
            AuthenticationController authenticationController,
            YummySharedPreferences yummySharedPreferences,
            @Nullable UserSessionView userSessionView,
            @Nullable AuthenticatorActivityView loginView,
            @Nullable BaseActivityView baseActivityView) {

        this.context = context;
        this.authenticationController = authenticationController;
        this.sharedPreferences = yummySharedPreferences;
        this.userSessionView = userSessionView;
        this.loginView = loginView;
        this.baseActivityView = baseActivityView;
    }

    /**
     * Perform the authentication of the user, and handle the success response, or handle any error related
     * @param username
     * @param password
     */
    public void authenticate(String username, String password) {
        authenticationController.login(username, password, new ResultListener<AuthenticationController.LogInResponse>() {

            @Override
            public void onResult(AuthenticationController.LogInResponse result) {
                if (result.isSuccess()) {
                    loginView.showLoginSuccess(result.getPayload());
                } else {

                    if (HttpResponseCode.UNPROCESSABLE_ENTITY == result.getError().getCode()
                            && !result.getError().getErrors().isEmpty()) {

                        loginView.showLoginError(result.getError().getErrors().get(0).getMessage());
                    } else {
                        loginView.showLoginError(
                                String.format(
                                        context.getString(R.string.general_error), result.getError().getMessage()));
                    }
                }
            }
        });
    }

    /**
     * Log outs the current user and handle the response
     */
    public void logOutUser() {
        authenticationController.logOutUser(
            new ResultListener<AuthenticationController.LogOutResponse>() {
                @Override
                public void onResult(AuthenticationController.LogOutResponse result) {
                    if (result.isSuccess()) {
                        baseActivityView.showLoginScreen();
                    } /*else {
                        //FIXME find the final copy.
                        commonView.showMessage(getContext().getString(R.string.general_error, result.getError().getMessage()));
                    }*/
                }
            }
        );
    }

    /**
     * Validate if the user is logged in, and call to the properly view methods
     */
    public void validateUserLoggedIn() {
        User user = authenticationController.getUserLoggedIn();
        if (user == null) {
            baseActivityView.showLoginScreen();
        } else {
            userSessionView.showUserInfo(user);
        }
    }

    /**
     * Get the email that the user selected to be remembered
     * @return The email stored
     */
    public String getEmailStored() {
        return sharedPreferences.getEmail();
    }

    /**
     * Set the email that the user selected to be remembered
     * @param email Email to be stored
     * @param store true If the user want to store the email, otherwise the email will be deleted
     */
    public void setEmailStored(String email, boolean store) {
        sharedPreferences.setEmail(store ? email : "");
    }
}
