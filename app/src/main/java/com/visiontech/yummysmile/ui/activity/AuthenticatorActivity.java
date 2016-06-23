package com.visiontech.yummysmile.ui.activity;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.fragments.CreateAccountFragment;
import com.visiontech.yummysmile.ui.fragments.LoginFragment;
import com.visiontech.yummysmile.ui.fragments.RecoverPasswordFragment;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;

/**
 * @author manuel.ortiz
 *
 * Activity that shows the login screen
 *
 */
public class AuthenticatorActivity extends BaseActivity implements AuthenticatorActivityView {
    private static final String TAG_FRAGMENT_LOGIN = "LOGIN_FRAGMENT";
    private static final String TAG_FRAGMENT_RECOVER_PASSWORD = "RECOVER_PASSWORD_FRAGMENT";
    private static final String TAG_FRAGMENT_CREATE_ACCOUNT = "CREATE_ACCOUNT_FRAGMENT";

    public static final String ARG_ACCOUNT_TYPE = "ARG_ACCOUNT_TYPE";
    public static final String ARG_AUTH_TYPE = "ARG_AUTH_TYPE";
    public static final String ARG_IS_ADDING_NEW_ACCOUNT = "ARG_IS_ADDING_NEW_ACCOUNT";
    public static final String YUMMY_ACCOUNT_TYPE = "com.visiontech.yummysmile.normalaccount";
    public static final String NORMAL_USER_TOKEN_TYPE = "NORMAL_USER";

    private LoginFragment loginFragment;
    private RecoverPasswordFragment recoverPasswordFragment;
    private CreateAccountFragment createAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_authenticator);

        if (savedInstanceState == null) {
            loginFragment = LoginFragment.newInstance();
            recoverPasswordFragment = RecoverPasswordFragment.newInstance();
            createAccountFragment = CreateAccountFragment.newInstance();

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.authenticator_content_frame, loginFragment, TAG_FRAGMENT_LOGIN)
                    .addToBackStack(null)
                    .commit();
        } else {
            loginFragment = (LoginFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAG_FRAGMENT_LOGIN);
            recoverPasswordFragment = (RecoverPasswordFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAG_FRAGMENT_RECOVER_PASSWORD);
            createAccountFragment = (CreateAccountFragment) getSupportFragmentManager().getFragment(savedInstanceState, TAG_FRAGMENT_CREATE_ACCOUNT);
        }
    }

    @Override
    public void showLoginError(String errorMessage) {
        loginFragment.showProgress(false);

        //FIXME show the snake bar generic error message
        Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showLoginSuccess(User user) {
        //FIXME Clear from backstack the login fragment
        loginFragment.showProgress(false);

        Intent intentSaved = saveAuthenticationData(user);
        setResult(RESULT_OK, intentSaved);

        /* Create an Intent that will start the Home-Activity. */
        Intent mainIntent = new Intent(AuthenticatorActivity.this, HomeActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    public void navigateToRecoverPassword() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authenticator_content_frame, recoverPasswordFragment, TAG_FRAGMENT_RECOVER_PASSWORD)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToCreateAccount() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.authenticator_content_frame, createAccountFragment, TAG_FRAGMENT_RECOVER_PASSWORD)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void navigateToLogin() {
        //FIXME Implements this method correctly
        showMessage("Navigating to log in screen");
    }

    private Intent saveAuthenticationData(User user) {

        final Intent intent = new Intent();
        intent.putExtra(AccountManager.KEY_ACCOUNT_NAME, user.getEmail());
        intent.putExtra(AccountManager.KEY_ACCOUNT_TYPE, YUMMY_ACCOUNT_TYPE);
        intent.putExtra(AccountManager.KEY_AUTHTOKEN, user.getToken());

        return intent;
    }
}
