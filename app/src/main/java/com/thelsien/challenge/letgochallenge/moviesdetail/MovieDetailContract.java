package com.thelsien.challenge.letgochallenge.moviesdetail;

import com.thelsien.challenge.letgochallenge.models.MovieModel;

public interface MovieDetailContract {
    interface View {
        void onMovieDetailLoaded(MovieModel movie);
        void onError(Throwable error);
    }

    interface Presenter {
        void getMovieDetail(int movieId);
    }
}
