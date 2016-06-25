package com.visiontech.yummysmile.ui.presenter.view.activity;

import com.visiontech.yummysmile.models.User;

/**
 * @author manuel.ortiz on 23/06/16.
 */
public interface DrawerActivityView {
    /**
     * Shows the user profile information in the drawer
     * @param user
     */
    void showUserInfo(User user);
}
