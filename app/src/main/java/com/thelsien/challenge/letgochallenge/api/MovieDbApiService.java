package com.thelsien.challenge.letgochallenge.api;

import com.thelsien.challenge.letgochallenge.models.TopRatedModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;

public interface MovieDbApiService {
    static final String BASE_URL = "https://api.themoviedb.org/3/";
    static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    static final String API_KEY = "MYAPIKEY";

    @GET("movie/top_rated")
    @Headers({
            "Content-type: application/json",
            "Accept: application/json"
    })
    Observable<TopRatedModel> getTopRatedMovies(@QueryMap Map<String, String> queryMap);
}
