package com.visiontech.yummysmile.repository.api;

import com.squareup.okhttp.RequestBody;
import com.visiontech.yummysmile.repository.api.dto.MealDTO;
import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.util.Constants;

import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.Part;
import rx.Observable;

/**
 * Interface that contains the methods to use on Meal API service
 *
 * @author hector.torres
 */
public interface MealAPIService {
    //e.g. yummysmile/api/v1/meals
    @GET(Constants.MEALS)
    Observable<MealsDTO> getMeals(
            @Header(Constants.TOKEN_FIELD) String token,
            @Header(Constants.USER_FIELD) String userId
    );

    @Multipart
    @POST(Constants.MEALS)
    Observable<MealDTO> createMeal(
            @Header(Constants.TOKEN_FIELD) String token,
            @Header(Constants.USER_FIELD) String userId,
            @Part("file\"; filename=\"temp.png ") RequestBody file,
            @Part("meal") MealDTO meal);

}
