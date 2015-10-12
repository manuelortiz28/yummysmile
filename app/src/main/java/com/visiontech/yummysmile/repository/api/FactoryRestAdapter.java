package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.util.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * @author hector.torres
 */
public class FactoryRestAdapter {
    public static <T> T createRetrofitService(final Class<T> clazz) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(Constants.API_URL) // we can parameterize this if it is need it.
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //Rx java adapter
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }
}
