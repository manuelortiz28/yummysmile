package com.visiontech.yummysmile.ui.presenter;

import android.support.annotation.Nullable;

import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;

import javax.inject.Inject;

/**
 * @author manuel.ortiz
 * Presenter for BaseFragment class
 *
 */
public class BaseFragmentPresenter {
    private final AuthenticationController authenticationController;
    private final BaseActivityView baseActivityView;

    @Inject
    public BaseFragmentPresenter(
            AuthenticationController authenticationController,
            @Nullable BaseActivityView baseActivityView) {

        this.authenticationController = authenticationController;
        this.baseActivityView = baseActivityView;
    }

    /**
     * Returns the user logged in, if it is not, then display the login screen.
     */
    public User validateUserLoggedIn() {
        User user = authenticationController.getUserLoggedIn();
        if (user != null) {
            return user;
        }

        baseActivityView.showLoginScreen();
        return null;
    }
}
