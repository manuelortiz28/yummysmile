package com.visiontech.yummysmile.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.visiontech.yummysmile.R;
import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.ui.presenter.LoginPresenter;
import com.visiontech.yummysmile.ui.presenter.view.activity.AuthenticatorActivityView;
import com.visiontech.yummysmile.ui.presenter.view.fragment.LoginView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import dagger.Provides;

/**
 * @author manuel.ortiz on 08/06/16.
 */
public class LoginFragment extends BaseFragment implements LoginView {

    private EditText txtUsername;
    private EditText txtPassword;
    private Switch switchRememberUser;
    private LoginPresenter loginPresenter;
    private LoginButton loginButton;
    private AuthenticatorActivityView authenticatorActivityView;
    private CallbackManager callbackManager;
    private ProfileTracker profileTracker;
    private User currentUser;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container, Bundle saveInstance) {
        FacebookSdk.sdkInitialize(this.getContext());

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

        loginButton = (LoginButton) viewParent.findViewById(R.id.btnFacebookLogin);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));

        return viewParent;
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);

        callbackManager = coreComponent.getCallBackManager();
        authenticatorActivityView = activityPresenterComponent.getAuthenticatorActivityView();
        loginPresenter = fragmentPresenterComponent.getLoginPresenter();

        //Remember username functionality
        final String userNameStored = loginPresenter.getEmailStored();
        txtUsername.setText(userNameStored);
        switchRememberUser.setChecked(!TextUtils.isEmpty(userNameStored));

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                if (currentProfile == null) {
                    return;
                }

                boolean tryToLogin = false;
                if (currentUser == null) {
                    currentUser = new User();
                } else {
                    tryToLogin = true;
                }

                currentUser.setName(currentProfile.getFirstName());
                currentUser.setLastName(currentProfile.getLastName());

                if (tryToLogin) {
                    showProgress(true);
                    loginPresenter.authenticateWithSocialNetwork(currentUser);
                    currentUser = null;
                }
            }
        };

        loginButton.registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {

                        // App code
                        GraphRequest request = GraphRequest.newMeRequest(
                                loginResult.getAccessToken(),
                                new GraphRequest.GraphJSONObjectCallback() {
                                    @Override
                                    public void onCompleted(JSONObject object, GraphResponse response) {
                                        Log.v("LoginActivity", response.toString());

                                        profileTracker.startTracking();

                                        AccessToken accessToken = loginResult.getAccessToken();

                                        boolean tryToLogin = false;

                                        if (currentUser == null) {
                                            currentUser = new User();
                                        } else {
                                            tryToLogin = true;
                                        }

                                        currentUser.setToken(accessToken.getToken());
                                        currentUser.setSocialNetworkUserId(accessToken.getUserId());
                                        currentUser.setSocialNetworkType("fb");
                                        try {
                                            currentUser.setEmail(object.getString("email"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        if (tryToLogin) {
                                            showProgress(true);
                                            loginPresenter.authenticateWithSocialNetwork(currentUser);
                                            currentUser = null;
                                        }
                                    }
                                });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        showMessage("On Cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        showMessage("Error " + error.getMessage());
                    }
                });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        profileTracker.stopTracking();
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

    @Override
    public void showLoginSuccess(User user) {
        showProgress(false);
        authenticatorActivityView.navigateToHome(user);
    }

    @Override
    public void showLoginError(String errorMessage) {
        showProgress(false);
        showMessage(errorMessage);
    }
}
