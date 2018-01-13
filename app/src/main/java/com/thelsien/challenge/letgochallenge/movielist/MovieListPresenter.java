package com.thelsien.challenge.letgochallenge.movielist;

import android.util.Log;

import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.api.RetrofitHelper;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieListPresenter implements MovieListContract.Presenter {

    private static final String TAG = MovieListPresenter.class.getSimpleName();

    private MovieListContract.View mMovieListView;

    public MovieListPresenter(MovieListContract.View movieListView) {
        this.mMovieListView = movieListView;
    }

    @Override
    public void getTopRatedMovies(int page) {
        Map<String, String> queryMap = new HashMap<>();

        queryMap.put("api_key", MovieDbApiService.API_KEY);
        queryMap.put("page", String.valueOf(page));

        RetrofitHelper.getMovieDbApiService().getTopRatedMovies(queryMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        topRatedModel -> mMovieListView.onTopRatedMoviesGet(topRatedModel.results),
                        error -> {
                            Log.e(TAG, "getTopRatedMovies: error", error);
                        });
    }
}
