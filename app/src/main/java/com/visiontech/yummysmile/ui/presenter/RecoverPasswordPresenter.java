package com.visiontech.yummysmile.ui.presenter;

import android.support.annotation.Nullable;

import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.fragment.RecoverPasswordFragmentView;

import javax.inject.Inject;

/**
 * @author manuel.ortiz on 11/06/16.
 */
public class RecoverPasswordPresenter {
    private final AuthenticationController authenticationController;
    private final RecoverPasswordFragmentView recoverPasswordView;

    @Inject
    public RecoverPasswordPresenter(
            AuthenticationController authenticationController,
            @Nullable RecoverPasswordFragmentView recoverPasswordView) {

        this.authenticationController = authenticationController;
        this.recoverPasswordView = recoverPasswordView;
    }

    public void recoverPassword(String email) {
        authenticationController.recoverPassword(email, new ResultListener<AuthenticationController.RecoverPasswordResponse>() {
            @Override
            public void onResult(AuthenticationController.RecoverPasswordResponse result) {
                if (result.isSuccess()) {
                    recoverPasswordView.showRecoverPasswordSuccess();
                } else {
                    recoverPasswordView.showRecoverPasswordError();
                }
            }
        });
    }
}
