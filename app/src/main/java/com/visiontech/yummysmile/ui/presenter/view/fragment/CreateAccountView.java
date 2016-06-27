package com.visiontech.yummysmile.ui.presenter.view.fragment;

/**
 * @author manuel.ortiz on 18/06/16.
 */
public interface CreateAccountView {
    /**
     * This method is called when the create account service was successful
     */
    void showCreateAccountSuccess();

    /**
     * Shows an error message indicating the reason for the create account could not be performed
     */
    void showCreateAccountError(String message);
}
