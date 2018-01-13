package com.thelsien.challenge.letgochallenge.api;

import com.thelsien.challenge.letgochallenge.models.TopRatedModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface MovieDbApiService {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    String API_KEY = "MYAPIKEY";

    @GET("movie/top_rated")
    Observable<TopRatedModel> getTopRatedMovies(@QueryMap Map<String, String> queryMap);
}
