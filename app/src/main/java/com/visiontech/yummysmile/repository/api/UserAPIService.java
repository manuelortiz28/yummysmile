package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.repository.api.dto.UserDTO;

import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Interface that contains the methods to use on Meal API service
 *
 * @author hector.torres
 */
public interface UserAPIService {
    @FormUrlEncoded
    @POST("/yummysmile/api/login")
    UserDTO login();
}
