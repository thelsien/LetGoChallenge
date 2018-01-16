package com.thelsien.challenge.letgochallenge.api;

import com.thelsien.challenge.letgochallenge.models.MovieDetailModel;
import com.thelsien.challenge.letgochallenge.models.MovieListModel;

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
    Observable<MovieListModel> getTopRatedMovies(@QueryMap Map<String, String> queryMap);

    @GET("movie/{movieId}")
    Observable<MovieDetailModel> getMovieDetail(@Path("movieId") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{movieId}/similar")
    Observable<MovieListModel> getSimilarMovies(@Path("movieId") int movieId, @Query("api_key") String apiKey, @Query("page") int page);
}
