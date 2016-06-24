package com.visiontech.yummysmile.ui.presenter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.response.HttpResponseCode;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;
import com.visiontech.yummysmile.ui.controller.AuthenticationController;
import com.visiontech.yummysmile.ui.presenter.view.fragment.BaseFragmentView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateAccountView;

import javax.inject.Inject;

/**
 * @author manuel.ortiz on 18/06/16.
 */
public class CreateAccountPresenter {
    private final Context context;
    private final AuthenticationController authenticationController;
    private final CreateAccountView createAccountView;
    private final BaseFragmentView baseFragmentView;

    @Inject
    public CreateAccountPresenter(
            Context context,
            AuthenticationController authenticationController,
            @Nullable CreateAccountView createAccountView,
            @Nullable BaseFragmentView baseFragmentView) {

        this.context = context;
        this.authenticationController = authenticationController;
        this.createAccountView = createAccountView;
        this.baseFragmentView = baseFragmentView;
    }

    public void createUserAccount(String firstName, String lastName, String email, String password) {
        User user = new User();
        user.setName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);

        authenticationController.createAccount(user, password, new ResultListener<AuthenticationController.CreateAccountResponse>() {
            @Override
            public void onResult(AuthenticationController.CreateAccountResponse result) {
                if (result.isSuccess()) {
                    createAccountView.showCreateAccountSuccess();
                } else {

                    if (HttpResponseCode.CONFLICT == result.getError().getCode()) {
                        createAccountView.showCreateAccountError(context.getString(R.string.create_account_user_existing));
                    } else {
                        createAccountView.showCreateAccountError(context.getString(R.string.create_account_error));
                    }
                }
            }
        });
    }

    public boolean isDataValid(
            String firstName,
            String lastName,
            String username,
            String password,
            String confirmationPassword) {

        if (TextUtils.isEmpty(firstName)
                || TextUtils.isEmpty(lastName)
                || TextUtils.isEmpty(username)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(confirmationPassword)) {

            baseFragmentView.showMessage(context.getString(R.string.create_account_empty_fields));
            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
            baseFragmentView.showMessage(context.getString(R.string.invalid_email));
            return false;
        }

        return true;
    }
}
