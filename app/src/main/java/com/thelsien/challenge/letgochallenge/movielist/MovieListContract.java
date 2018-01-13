package com.thelsien.challenge.letgochallenge.movielist;

import com.thelsien.challenge.letgochallenge.models.TopRatedModel;

public interface MovieListContract {
    interface View {
        void onTopRatedMoviesLoaded(TopRatedModel topRatedModel);
        void showError(Throwable error);
    }

    interface Presenter {
        void getTopRatedMovies(int page);
    }
}
