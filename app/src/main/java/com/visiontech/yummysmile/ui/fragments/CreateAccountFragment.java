package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.ui.presenter.CreateAccountPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.CreateAccountView;
import com.visiontech.yummysmile.util.UIHelper;

/**
 * @author manuel.ortiz on 18/06/16.
 */
public class CreateAccountFragment extends BaseFragment implements CreateAccountView {
    private static final String IS_CREATE_ACCOUNT_SUCCESS = "IS_CREATE_ACCOUNT_SUCCESS";

    private boolean isCreateAccountSuccess;

    private CreateAccountPresenter createAccountPresenter;
    private View viewContainer;
    private View viewSuccess;

    public static CreateAccountFragment newInstance() {
        return new CreateAccountFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle bundle) {
        View viewParent = layoutInflater.inflate(R.layout.fragment_create_account_layout, container, false);

        viewContainer = viewParent.findViewById(R.id.viewContainer);
        viewSuccess = viewParent.findViewById(R.id.create_account_success);

        final TextInputEditText txtFirstName = (TextInputEditText) viewParent.findViewById(R.id.txtFirstName);
        final TextInputEditText txtLastName = (TextInputEditText) viewParent.findViewById(R.id.txtLastName);
        final TextInputEditText txtUsername = (TextInputEditText) viewParent.findViewById(R.id.txtUsername);
        final TextInputEditText txtPassword = (TextInputEditText) viewParent.findViewById(R.id.txtPassword);
        final TextInputEditText txtConfirmPassword = (TextInputEditText) viewParent.findViewById(R.id.txtConfirmationPassword);

        //Login button
        viewParent.findViewById(R.id.btnCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (createAccountPresenter.isDataValid(
                        txtFirstName.getText().toString().trim(),
                        txtLastName.getText().toString().trim(),
                        txtUsername.getText().toString().trim(),
                        txtPassword.getText().toString().trim(),
                        txtConfirmPassword.getText().toString().trim())) {

                    UIHelper.hideKeyboard(getActivity());

                    viewContainer.setVisibility(View.GONE);
                    showProgress(true);

                    createAccountPresenter.createUserAccount(
                            txtFirstName.getText().toString(),
                            txtLastName.getText().toString(),
                            txtUsername.getText().toString(),
                            txtPassword.getText().toString()
                    );
                }
            }
        });

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        createAccountPresenter = fragmentPresenterComponent.getCreateAccountPresenter();

        if (bundle != null) {
            isCreateAccountSuccess = bundle.getBoolean(IS_CREATE_ACCOUNT_SUCCESS);

            viewContainer.setVisibility(isCreateAccountSuccess ? View.GONE : View.VISIBLE);
            viewSuccess.setVisibility(isCreateAccountSuccess ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstance) {
        saveInstance.putBoolean(IS_CREATE_ACCOUNT_SUCCESS, isCreateAccountSuccess);
    }

    @Override
    public void showCreateAccountSuccess() {
        showProgress(false);
        viewSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCreateAccountError(String errorMessage) {
        showProgress(false);
        viewContainer.setVisibility(View.VISIBLE);
        showMessage(errorMessage);
    }
}
