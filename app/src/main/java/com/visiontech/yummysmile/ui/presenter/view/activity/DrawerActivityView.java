package com.visiontech.yummysmile.ui.presenter.view.activity;

import com.visiontech.yummysmile.models.User;

/**
 * @author montm243
 */
public interface DrawerActivityView {
    /**
     * Opens the login screen
     */
    void showLoginScreen();

    /**
     * Shows the user profile information in the drawer
     * @param user
     */
    void showUserInfo(User user);
}
