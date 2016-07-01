package com.visiontech.yummysmile.ui.controller;

import com.visiontech.yummysmile.models.User;
import com.visiontech.yummysmile.repository.api.response.BaseResponse;
import com.visiontech.yummysmile.repository.api.subscriber.ResultListener;

/**
 * @author manuel ortiz
 *
 * Controller that handles all the operations related to the authentication of the user
 *
 */
public interface AuthenticationController {
    String USER_ID = "USER_ID_DATA";
    String USER_NAME = "USER_NAME_DATA";
    String USER_LAST_NAME = "USER_LAST_NAME_DATA";

    /**
     * Creates a new user account.
     * @param user Object containing all the user information
     * @param password Password to set to this account
     * @param resultListener Listener used as a callback to pass the result, which contain an User model object
     */
    void createAccount(User user, String password, ResultListener<CreateAccountResponse> resultListener);

    /**
     * Invoke the login api method, in order of trying to log in to the user
     * @param username Username
     * @param password Password of the account
     * @param result Listener used as a callback to pass the result, which contain an User model object
     */
    void login(String username, String password, ResultListener<LogInResponse> result);

    /**
     * Invoke the login api method, in order of trying to log in to the user using a social network
     * @param user User model containing all its information
     * @param result Listener used as a callback to pass the result, which contain an User model object
     */
    void loginWithSocialNetwork(User user, ResultListener<LogInResponse> result);

    /**
     * Gets the current user logged in, as an User object model
     * @return The user logged in if there is one, otherwise null
     */
    User getUserLoggedIn();

    /**
     * Log out the current user
     * @param resultListener Listener used as a callback to pass the result
     */
    void logOutUser(ResultListener<LogOutResponse> resultListener);

    /**
     * Sends an email for re assigning a new password for the given email account
     * @param email
     */
    void recoverPassword(String email, ResultListener<RecoverPasswordResponse> resultListener);


    //===========================================================================================================
    //===============================================   Events    ===============================================
    //===========================================================================================================

    class CreateAccountResponse extends BaseResponse<User> {
    }

    /**
     * Response object containing an User object
     */
    class LogInResponse extends BaseResponse<User> {
    }

    class LogOutResponse extends BaseResponse {
    }

    class RecoverPasswordResponse extends BaseResponse {
    }
}
