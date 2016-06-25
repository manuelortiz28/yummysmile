package com.visiontech.yummysmile.ui.presenter.view;

import android.content.Context;
import android.support.annotation.Nullable;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.activity.BaseActivityView;
import com.visiontech.yummysmile.ui.presenter.view.activity.DrawerActivityView;

import javax.inject.Inject;

/**
 * @author manuel.ortiz on 23/06/16.
 */
public class DrawerPresenter {

    private final Context context;
    private final AuthenticationController authenticationController;
    private final DrawerActivityView drawerActivityView;
    private final BaseActivityView baseActivityView;

    @Inject
    public DrawerPresenter(
            Context context,
            AuthenticationController authenticationController,
            @Nullable DrawerActivityView drawerActivityView,
            @Nullable BaseActivityView baseActivityView) {

        this.context = context;
        this.authenticationController = authenticationController;
        this.drawerActivityView = drawerActivityView;
        this.baseActivityView = baseActivityView;
    }

    /**
     * Log outs the current user and handle the response
     */
    public void logOutUser() {
        authenticationController.logOutUser(
                new ResultListener<AuthenticationController.LogOutResponse>() {
                    @Override
                    public void onResult(AuthenticationController.LogOutResponse result) {
                        if (result.isSuccess()) {
                            baseActivityView.showLoginScreen();
                        } else {
                            baseActivityView.showMessage(context.getString(R.string.general_error, result.getError().getMessage()));
                        }
                    }
                }
        );
    }

    /**
     * Validate if the user is logged in, and call to the properly view methods
     */
    public void retrieveUserInformation() {
        User user = authenticationController.getUserLoggedIn();
        if (user != null) {
            drawerActivityView.showUserInfo(user);
        }
    }
}
