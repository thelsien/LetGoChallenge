package com.thelsien.challenge.letgochallenge.moviesdetail;

import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.api.RetrofitHelper;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailPresenter implements MovieDetailContract.Presenter {

    private MovieDetailContract.View mDetailView;

    public MovieDetailPresenter(MovieDetailContract.View movieDetailView) {
        this.mDetailView = movieDetailView;
    }

    @Override
    public void getMovieDetail(int movieId) {
        RetrofitHelper.getMovieDbApiService().getMovieDetail(movieId, MovieDbApiService.API_KEY)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                        movieModel -> mDetailView.onMovieDetailLoaded(movieModel),
                        error -> mDetailView.onError(error)
                );
    }
}
