package com.thelsien.challenge.letgochallenge.moviesdetail;


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
import com.thelsien.challenge.letgochallenge.models.MovieModel;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private MovieDetailContract.Presenter mPresenter;
    private MovieModel mMovieWithDetails;
    private MovieListResultModel mMovieFromList;

    private int mMovieId;

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    public static MovieDetailFragment createNewInstance(int movieId) {
        MovieDetailFragment detailFragment = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putInt("movieId", movieId);

        detailFragment.setArguments(args);

        return detailFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            mMovieId = args.getInt("movieId");
        }

        mPresenter = new MovieDetailPresenter(this);
        mPresenter.getMovieDetail(mMovieId);
    }

    @Override
    public void onMovieDetailLoaded(MovieModel movie) {
        Log.d(TAG, "onMovieDetailLoaded: overview: " + movie.overview);
    }

    @Override
    public void onError(Throwable error) {
        Log.e(TAG, "onError: error", error);
    }
}
