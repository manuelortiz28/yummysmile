package com.visiontech.yummysmile.ui.presenter.view.fragment;

import com.visiontech.yummysmile.models.User;

/**
 * @author manuel.ortiz on 23/06/16.
 */
public interface LoginView {
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
}
