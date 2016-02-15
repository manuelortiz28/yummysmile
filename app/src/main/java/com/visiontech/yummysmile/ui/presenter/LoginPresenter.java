package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.controller.AuthenticationControllerImpl;

import javax.inject.Inject;

/**
 * @author manuel ortiz
 *
 * Class in the Presenter tier from the MVP pattern, containing methods related to the user login
 *
 */
public class LoginPresenter {
    private final LoginView loginView;
    private final AuthenticationController authenticationController;
    private final Context context;

    @Inject
    public LoginPresenter(LoginView loginView, AuthenticationControllerImpl authenticationController, Context context) {

        this.loginView = loginView;
        this.authenticationController = authenticationController;
        this.context = context;
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
}
