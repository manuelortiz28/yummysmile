package com.visiontech.yummysmile.ui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.di.components.ActivityPresenterComponent;
import com.visiontech.yummysmile.di.components.FragmentPresenterComponent;
import com.visiontech.yummysmile.ui.activity.BaseActivity;
import com.visiontech.yummysmile.ui.presenter.LoginPresenter;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;

/**
 * @author manuel.ortiz on 08/06/16.
 */
public class LoginFragment extends BaseFragment {

    private EditText txtUsername;
    private EditText txtPassword;
    private Switch switchRememberUser;
    private LoginPresenter loginPresenter;
    private AuthenticatorActivityView authenticatorActivityView;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstance) {
        View viewParent = layoutInflater.inflate(R.layout.fragment_login_layout, container, false);

        txtUsername = (EditText) viewParent.findViewById(R.id.txtUsername);
        txtPassword = (EditText) viewParent.findViewById(R.id.txtPassword);
        switchRememberUser = (Switch) viewParent.findViewById(R.id.switch_remember_username);

        //Login button
        viewParent.findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isDataValid()) {
                    showProgress(true);
                    loginPresenter.setEmailStored(txtUsername.getText().toString(), switchRememberUser.isChecked());
                    loginPresenter.authenticate(txtUsername.getText().toString(), txtPassword.getText().toString());
                }
            }
        });

        switchRememberUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loginPresenter.setEmailStored(txtUsername.getText().toString(), isChecked);
            }
        });

        viewParent.findViewById(R.id.txtForgotPassword).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticatorActivityView.navigateToRecoverPassword();
            }
        });

        viewParent.findViewById(R.id.txtCreateAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authenticatorActivityView.navigateToCreateAccount();
            }
        });

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        ActivityPresenterComponent activityPresenterComponent =
                application.getActivityPresenterComponent((BaseActivity) getActivity());

        FragmentPresenterComponent fragmentPresenterComponent =
                application.getFragmentPresenterComponent(this, activityPresenterComponent);

        authenticatorActivityView = activityPresenterComponent.getAuthenticatorActivityView();
        loginPresenter = fragmentPresenterComponent.getLoginPresenter();

        //Remember username functionality
        final String userNameStored = loginPresenter.getEmailStored();
        txtUsername.setText(userNameStored);
        switchRememberUser.setChecked(!TextUtils.isEmpty(userNameStored));
    }

    //FIXME Should this method be moved to the presenter class?
    private boolean isDataValid() {
        if (TextUtils.isEmpty(txtUsername.getText().toString().trim())
                || TextUtils.isEmpty(txtPassword.getText().toString().trim())) {

            showMessage(getString(R.string.login_error_fields_empty));

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtUsername.getText().toString().trim()).matches()) {
            showMessage(getString(R.string.invalid_email));

            return false;
        }

        return true;
    }
}
