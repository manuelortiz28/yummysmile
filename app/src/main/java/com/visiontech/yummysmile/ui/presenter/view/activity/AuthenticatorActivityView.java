package com.visiontech.yummysmile.ui.presenter.view.activity;

import com.visiontech.yummysmile.models.User;

/**
 * @author manuel.ortiz
 *
 * Class in the View tier from the MVP pattern, containing methods related to the user login
 *
 */
public interface AuthenticatorActivityView {

    /**
     * This method is called when the log in was successful
     * @param user The user who logged in
     */
    void showLoginSuccess(User user);

    /**
     * Shows an error message indicating the reason for the log in could not be performed
     * @param errorMessage Error message to show
     */
    void showLoginError(String errorMessage);

    /**
     * Navigates to the recover password screen
     */
    void navigateToRecoverPassword();

    /**
     * Navigates to create account screen.
     */
    void navigateToCreateAccount();

    /**
     * Navigates to Login screen
     */
    void navigateToLogin();
}
