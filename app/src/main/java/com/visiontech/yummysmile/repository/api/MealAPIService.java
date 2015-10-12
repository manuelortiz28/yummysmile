package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.repository.api.dto.MealsDTO;
import com.visiontech.yummysmile.util.Constants;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Header;
import rx.Observable;

/**
 * @author hector.torres
 */
public interface MealAPIService {

    @GET("yummysmile/api/v1/meals")
    Observable<MealsDTO> getMeals(
            @Header(Constants.TOKEN_FIELD) String token,
            @Header(Constants.USER_FIELD) String userId
    );
}
