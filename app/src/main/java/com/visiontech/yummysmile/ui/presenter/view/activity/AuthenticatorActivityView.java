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
     * Navigates to the recover password screen
     */
    void navigateToRecoverPassword();

    /**
     * Navigates to create account screen.
     */
    void navigateToCreateAccount();

    /**
     * Navigates to home screen
     */
    void navigateToHome(User user);
}
