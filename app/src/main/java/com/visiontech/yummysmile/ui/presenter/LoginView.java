package com.visiontech.yummysmile.ui.presenter;

import com.visiontech.yummysmile.models.User;

/**
 * @author manuel.ortiz
 *
 * Class in the View tier from the MVP pattern, containing methods related to the user login
 *
 */
public interface LoginView {
    /**
     * Shows an error message indicating the reason for the log in could not be performed
     * @param errorMessage Error message to show
     */
    void showError(String errorMessage);

    /**
     * This method is called when the log in was successful
     * @param user The user who logged in
     */
    void showSuccess(User user);
}
