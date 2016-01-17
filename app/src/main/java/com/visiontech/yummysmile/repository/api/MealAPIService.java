package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.util.Constants;

import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

/**
 * Interface that contains the methods to use on Meal API service
 *
 * @author hector.torres
 */
public interface MealAPIService {
    ///e.g. yummysmile/api/v1/meals
    @GET(Constants.URI + Constants.API + Constants.VERSION_1 + Constants.MEALS)
    Observable<MealsDTO> getMeals(
            @Header(Constants.TOKEN_FIELD) String token,
            @Header(Constants.USER_FIELD) String userId
    );
}
