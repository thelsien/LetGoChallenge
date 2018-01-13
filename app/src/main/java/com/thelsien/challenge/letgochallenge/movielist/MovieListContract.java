package com.thelsien.challenge.letgochallenge.movielist;

import com.thelsien.challenge.letgochallenge.models.MovieListResultModel;

import java.util.List;

public interface MovieListContract {
    interface View {
        void onTopRatedMoviesGet(List<MovieListResultModel> movies);
    }

    interface Presenter {
        void getTopRatedMovies(int page);
    }
}
