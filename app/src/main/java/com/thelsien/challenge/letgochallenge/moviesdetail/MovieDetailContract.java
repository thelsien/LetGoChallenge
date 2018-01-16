package com.thelsien.challenge.letgochallenge.moviesdetail;

import com.thelsien.challenge.letgochallenge.models.MovieDetailModel;
import com.thelsien.challenge.letgochallenge.models.MovieListModel;

public interface MovieDetailContract {
    interface View {
        void onMovieDetailLoaded(MovieDetailModel movie);
        void onSimilarMoviesLoaded(MovieListModel movieListModel);
        void onDetailError(Throwable error);
        void onSimilarMoviesError(Throwable error);
    }

    interface Presenter {
        void getMovieDetail(int movieId);
        void getSimilarMovies(int movieId, int page);
    }
}
