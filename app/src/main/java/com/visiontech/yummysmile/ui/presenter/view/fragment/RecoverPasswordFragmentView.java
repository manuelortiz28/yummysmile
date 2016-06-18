package com.visiontech.yummysmile.ui.presenter.view.fragment;

/**
 * @author manuel.ortiz on 11/06/16.
 */
public interface RecoverPasswordFragmentView {
    /**
     * This method is called when the recover password service was successful
     */
    void showRecoverPasswordSuccess();

    /**
     * Shows an error message indicating the reason for the recover password could not be performed
     */
    void showRecoverPasswordError();
}
