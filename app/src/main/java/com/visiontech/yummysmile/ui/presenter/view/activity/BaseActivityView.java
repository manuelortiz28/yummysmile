package com.visiontech.yummysmile.ui.presenter.view.activity;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

/**
 * @author manuel.ortiz on 02/03/16.
 */
public interface BaseActivityView {
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

    /**
     * Sets the toolbar up, setting a title and icon
     * @param title Title resource to be set to the toolbar
     * @param toolbarViewId Id of the toolbar view
     * @param icon Drawable resource to set to the toolbar as an icon
     */
    void setUpToolbar(@StringRes int title, @IdRes int toolbarViewId, @DrawableRes int icon);

    /**
     * Opens the login screen
     */
    void showLoginScreen();
}
