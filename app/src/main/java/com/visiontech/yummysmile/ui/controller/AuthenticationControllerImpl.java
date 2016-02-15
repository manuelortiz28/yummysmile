package com.visiontech.yummysmile.ui.controller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.models.transformers.UserTransform;
import com.visiontech.yummysmile.repository.api.FactoryRestAdapter;
import com.visiontech.yummysmile.repository.api.UserAPIService;
import com.visiontech.yummysmile.repository.api.dto.UserDTO;
import com.visiontech.yummysmile.ui.activity.AuthenticatorActivity;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.repository.api.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

import javax.inject.Inject;

/**
 * @author manuel ortiz
 *
 * Controller that handles all the operations related to the authentication of the user
 *
 */
public class AuthenticationControllerImpl implements AuthenticationController {

    private final UserAPIService userAPIService;
    private final Context context;

    @Inject
    public AuthenticationControllerImpl(UserAPIService userAPIService, Context context) {
        this.userAPIService = userAPIService;
        this.context = context;
    }

    @Override
    public void login(
            String username,
            final String password,
            final ResultListener<AuthenticationControllerImpl.LoggedInResponse> resultListener) {

        final UserDTO userDTO = new UserDTO();
        userDTO.setEmail(username);
        userDTO.setPassword(password);

        final LoggedInResponse loggedInResponse = new LoggedInResponse();

        FactoryRestAdapter.invokeService(
                userAPIService.login(userDTO),
                new BaseSubscriber<UserDTO>(resultListener, loggedInResponse) {
                    @Override
                    protected void onSuccess(UserDTO serviceResponse) {

                        loggedInResponse.setPayload(UserTransform.getTransformUserDtoToUser().apply(serviceResponse));
                        saveAccount(loggedInResponse.getPayload(), true, password);
                    }
                });
    }

    @Override
    public User getUserLoggedIn() {

        AccountManager accountManager = AccountManager.get(context);
        Account []accounts = accountManager.getAccountsByType(AuthenticatorActivity.YUMMY_ACCOUNT_TYPE);
        if (accounts == null || accounts.length == 0) {
            return null;
        }

        Account userAccount = accounts[0];
        String token = accountManager.peekAuthToken(userAccount, AuthenticatorActivity.NORMAL_USER_TOKEN_TYPE);

        if (TextUtils.isEmpty(token)) {
            return null;
        }

        final User newUser = new User();
        newUser.setEmail(userAccount.name);
        newUser.setToken(token);
        newUser.setId(accountManager.getUserData(userAccount, USER_ID));
        newUser.setName(accountManager.getUserData(userAccount, USER_NAME));
        newUser.setLastName(accountManager.getUserData(userAccount, USER_LAST_NAME));

        return newUser;
    }

    @Override
    public void logOutUser(ResultListener<LoggedOutResponse> resultListener) {
        final User userLoggedIn = getUserLoggedIn();

        String token = "";
        String userId = "";

        if (userLoggedIn != null) {
            token = userLoggedIn.getToken();
            userId = userLoggedIn.getId();
        }

        final LoggedOutResponse loggedOutResponse = new LoggedOutResponse();

        FactoryRestAdapter.invokeService(
                userAPIService.logout(token, userId),
                new BaseSubscriber(resultListener, loggedOutResponse) {
                    @Override
                    protected void onSuccess(Object serviceResponse) {
                        //Nothing to do here
                    }
                }
        );
    }

    /**
     * Method that saves an account into the android Authentication Manager
     * @param user User model containing its related information
     * @param isAddingNewAccount
     * @param password Password of the account
     */
    private void saveAccount(User user, boolean isAddingNewAccount, String password) {
        final Account account = new Account(user.getEmail(), AuthenticatorActivity.YUMMY_ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(context);

        if (isAddingNewAccount) {
            Bundle extraInformationBundle = new Bundle();
            extraInformationBundle.putString(USER_ID, user.getId());
            extraInformationBundle.putString(USER_NAME, user.getName());
            extraInformationBundle.putString(USER_LAST_NAME, user.getLastName());

            accountManager.addAccountExplicitly(account, password, extraInformationBundle);
            accountManager.setAuthToken(account, AuthenticatorActivity.NORMAL_USER_TOKEN_TYPE, user.getToken());
        } else {
            accountManager.setPassword(account, password);
        }
    }

    private void removeAccount(User user) {
        final Account account = new Account(user.getEmail(), AuthenticatorActivity.YUMMY_ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(context);

        accountManager.removeAccount(account, null, null);
    }

    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    /**
     * Response object containing an User object
     */
    public static class LoggedInResponse extends BaseResponse<User> {
    }

    public static class LoggedOutResponse extends BaseResponse {
    }
}
