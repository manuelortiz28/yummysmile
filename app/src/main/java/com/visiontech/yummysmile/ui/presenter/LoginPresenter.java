package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.YummySharedPreferences;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.fragment.LoginView;

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

    private final LoginView loginView;

    @Inject
    public LoginPresenter(
            Context context,
            AuthenticationController authenticationController,
            YummySharedPreferences yummySharedPreferences,
            @Nullable LoginView loginView) {

        this.context = context;
        this.authenticationController = authenticationController;
        this.sharedPreferences = yummySharedPreferences;
        this.loginView = loginView;
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
