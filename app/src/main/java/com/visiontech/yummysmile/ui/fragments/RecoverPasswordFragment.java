package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.presenter.RecoverPasswordPresenter;
import com.visiontech.yummysmile.ui.presenter.view.fragment.RecoverPasswordFragmentView;
import com.visiontech.yummysmile.util.UIHelper;

/**
 * @author manuel.ortiz on 11/06/16.
 */
public class RecoverPasswordFragment extends BaseFragment implements RecoverPasswordFragmentView {
    private static final String IS_RECOVERY_SUCCESS = "IS_RECOVERY_SUCCESS";

    private TextInputEditText txtUsername;
    private View viewForm;
    private View viewSuccess;
    private RecoverPasswordPresenter recoveryPasswordPresenter;

    private boolean isRecoverySuccess;

    public static RecoverPasswordFragment newInstance() {
        return new RecoverPasswordFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle saveInstance) {
        View viewParent = inflater.inflate(R.layout.fragment_recover_password_layout, container, false);

        txtUsername = (TextInputEditText) viewParent.findViewById(R.id.txtUsername);

        //Login button
        viewParent.findViewById(R.id.btnRecoverPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    UIHelper.hideKeyboard(getActivity());
                    showProgress(true);
                    recoveryPasswordPresenter.recoverPassword(txtUsername.getText().toString());
                }
            }
        });

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        viewForm = getView().findViewById(R.id.recover_password_form);
        viewSuccess = getView().findViewById(R.id.recover_password_success);

        FragmentPresenterComponent fragmentPresenterComponent =
                application.getFragmentPresenterComponent(this, (BaseActivity) getActivity());

        recoveryPasswordPresenter = fragmentPresenterComponent.getRecoverPasswordPresenter();

        if (bundle != null) {
            isRecoverySuccess = bundle.getBoolean(IS_RECOVERY_SUCCESS);

            viewForm.setVisibility(isRecoverySuccess ? View.GONE : View.VISIBLE);
            viewSuccess.setVisibility(isRecoverySuccess ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle saveInstance) {
        saveInstance.putBoolean(IS_RECOVERY_SUCCESS, isRecoverySuccess);
    }

    //FIXME Should this method be moved to the presenter class?
    private boolean isDataValid() {
        if (TextUtils.isEmpty(txtUsername.getText().toString().trim())) {
            showMessage(getString(R.string.recover_error_fields_empty));

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtUsername.getText().toString().trim()).matches()) {
            showMessage(getString(R.string.invalid_email));

            return false;
        }

        return true;
    }

    @Override
    public void showRecoverPasswordSuccess() {
        isRecoverySuccess = true;

        showProgress(false);
        viewForm.setVisibility(View.GONE);
        viewSuccess.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecoverPasswordError() {
        showProgress(false);
        showMessage(getString(R.string.recover_password_error));
    }
}
