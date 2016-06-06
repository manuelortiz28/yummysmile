package com.visiontech.yummysmile.ui.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.presenter.LoginPresenter;
import com.visiontech.yummysmile.ui.presenter.view.activity.LoginActivityView;

/**
 * @author manuel.ortiz
 *
 * Activity that shows the login screen
 *
 */
public class AuthenticatorActivity extends BaseActivity implements LoginActivityView {
    public static final String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    public static final String YUMMY_ACCOUNT_TYPE = "com.visiontech.yummysmile.normalaccount";
    public static final String NORMAL_USER_TOKEN_TYPE = "NORMAL_USER";

    private EditText txtUsername;
    private EditText txtPassword;
    private Switch switchRememberUser;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_screen);

        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
        switchRememberUser = (Switch) findViewById(R.id.switch_remember_username);

        loginPresenter = application.getActivityPresenterComponent(this).getLoginPresenter();

        //Remember username functionality
        final String userNameStored = loginPresenter.getEmailStored();
        txtUsername.setText(userNameStored);
        switchRememberUser.setChecked(!TextUtils.isEmpty(userNameStored));

        switchRememberUser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                loginPresenter.setEmailStored(txtUsername.getText().toString(), isChecked);
            }
        });
    }

    public void onLoginClick(View view) {
        if (isDataValid()) {
            showProgress(true);
            loginPresenter.setEmailStored(txtUsername.getText().toString(), switchRememberUser.isChecked());
            loginPresenter.authenticate(txtUsername.getText().toString(), txtPassword.getText().toString());
        }
    }

    @Override
    public void showError(String errorMessage) {
        showProgress(false);
        //FIXME show the snake bar generic error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showSuccess(User user) {
        showProgress(false);

        Intent intentSaved = saveAuthenticationData(user);
        setResult(RESULT_OK, intentSaved);

        /* Create an Intent that will start the Menu-Activity. */
        Intent mainIntent = new Intent(AuthenticatorActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
    }

    private boolean isDataValid() {
        if (TextUtils.isEmpty(txtUsername.getText().toString().trim())
                || TextUtils.isEmpty(txtPassword.getText().toString().trim())) {

            showError(getString(R.string.login_error_fields_empty));

            return false;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(txtUsername.getText().toString().trim()).matches()) {
            showError(getString(R.string.invalid_email));

            return false;
        }

        return true;
    }

    private Intent saveAuthenticationData(User user) {

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, YUMMY_ACCOUNT_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, user.getToken());

        return intent;
    }
}
