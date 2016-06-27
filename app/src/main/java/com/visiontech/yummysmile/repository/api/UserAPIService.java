package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.repository.api.dto.UserDTO;
import com.visiontech.yummysmile.util.Constants;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Query;
import rx.Observable;

/**
 * Interface that contains the methods to use on Meal API service
 *
 * @author hector.torres
 */
public interface UserAPIService {
    @POST("signup")
    Observable<UserDTO> createAccount(@Body UserDTO user);

    @POST("login")
    Observable<UserDTO> login(@Body UserDTO user);

    @GET("logout")
    Observable<String> logout(
            @Header(Constants.TOKEN_FIELD) String token,
            @Header(Constants.USER_FIELD) String userId);

    @POST("recoverpassword")
    Observable<String> recoverPassword(@Query("email") String email);
}
