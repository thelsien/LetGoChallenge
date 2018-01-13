package com.thelsien.challenge.letgochallenge.movielist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.models.MovieListResultModel;

import java.util.List;

public class MovieListFragment extends Fragment implements MovieListContract.View {

    private static final String TAG = MovieListFragment.class.getSimpleName();

    private MovieListContract.Presenter mPresenter;
    private int mPage = 1; //TODO savedInstanceState?

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new MovieListPresenter(this);
        mPresenter.getTopRatedMovies(mPage);
    }

    @Override
    public void onTopRatedMoviesGet(List<MovieListResultModel> movies) {
        for (MovieListResultModel movie : movies) {
            Log.d(TAG, "onTopRatedMoviesGet: " + movie.title);
        }
    }
}
