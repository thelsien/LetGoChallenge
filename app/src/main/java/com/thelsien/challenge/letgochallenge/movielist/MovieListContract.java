package com.thelsien.challenge.letgochallenge.movielist;

import com.thelsien.challenge.letgochallenge.models.MovieListModel;

public interface MovieListContract {
    interface View {
        void onTopRatedMoviesLoaded(MovieListModel movieListModel);
        void showError(Throwable error);
    }

    interface Presenter {
        void getTopRatedMovies(int page);
    }
}
