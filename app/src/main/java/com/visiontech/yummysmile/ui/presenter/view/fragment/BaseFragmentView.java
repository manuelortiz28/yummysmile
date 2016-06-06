package com.visiontech.yummysmile.ui.presenter.view.fragment;

/**
 * @author manuel.ortiz on 02/03/16.
 */
public interface BaseFragmentView {

    /**
     * Show the progress dialog
     * @param show If the progress should be displayed or not
     */
    void showProgress(boolean show);

    /**
     * Shows a message to the user
     * @param message
     */
    void showMessage(String message);
}
