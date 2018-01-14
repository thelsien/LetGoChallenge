package com.thelsien.challenge.letgochallenge.api;

import com.thelsien.challenge.letgochallenge.models.MovieModel;
import com.thelsien.challenge.letgochallenge.models.TopRatedModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MovieDbApiService {
    String BASE_URL = "https://api.themoviedb.org/3/";
    String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500/";
    String API_KEY = "214ff7ba5d022c92fe571a1f3bcb8b2c";

    @GET("movie/top_rated")
    Observable<TopRatedModel> getTopRatedMovies(@QueryMap Map<String, String> queryMap);

    @GET("movie/{movieId}")
    Observable<MovieModel> getMovieDetail(@Path("movieId") int movieId, @Query("api_key") String apiKey);
}
