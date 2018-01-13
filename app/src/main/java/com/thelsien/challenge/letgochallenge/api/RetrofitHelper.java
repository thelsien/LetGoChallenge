package com.thelsien.challenge.letgochallenge.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static Retrofit getGsonObservableRetrofit() {
        return new Retrofit.Builder()
                .baseUrl(MovieDbApiService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static MovieDbApiService getMovieDbApiService() {
        Retrofit retrofit = getGsonObservableRetrofit();

        return retrofit.create(MovieDbApiService.class);
    }
}
