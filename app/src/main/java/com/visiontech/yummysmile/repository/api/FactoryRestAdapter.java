package com.visiontech.yummysmile.repository.api;

import com.visiontech.yummysmile.repository.api.subscriber.BaseSubscriber;
import com.visiontech.yummysmile.util.Constants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Class that help to use on Rx Android to make a generic adapter with Gson
 *
 * @author hector.torres
 */
public final class FactoryRestAdapter {

    private FactoryRestAdapter() {
    }

    /**
     * Creates a Retrofit service using the given class type
     * @param clazz Class type of the retrofit service to create
     * @param <T> Type of the class
     * @return A retrofit service object
     */
    public static <T> T createRetrofitService(final Class<T> clazz) {
        final Retrofit restAdapter = new Retrofit.Builder()
                .baseUrl(Constants.HOST + Constants.URI + Constants.API + Constants.API_VERSION)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()) //Rx java adapter
                .build();
        T service = restAdapter.create(clazz);

        return service;
    }

    /**
     * Call to a observable object observing the main thread and, subscribing on Schedulers.io
     * with the subscriber parameter given
     *
     * @param observable Observable to invoke
     * @param subscriber Subscriber used in the invocation
     */
    public static void invokeService(Observable observable, BaseSubscriber subscriber) {
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
