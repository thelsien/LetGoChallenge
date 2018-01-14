package com.thelsien.challenge.letgochallenge.moviesdetail;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.models.MovieListResultModel;
import com.thelsien.challenge.letgochallenge.models.MovieModel;

import java.util.Locale;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();

    private MovieDetailContract.Presenter mPresenter;
    private MovieModel mMovieWithDetails;
    private MovieListResultModel mMovieFromList;

    private int mMovieId;

    private ImageView mPosterView;
    private TextView mOriginalTitleView;
    private TextView mOriginalLanguageView;
    private TextView mVoteAverageView;
    private TextView mRuntimeView;
    private TextView mOverviewView;
    private RecyclerView mRelatedMoviesListView;
    private ProgressBar mDetailsLoadingBar;
    private TextView mDetailsErrorView;
    private LinearLayout mDetailsWrapper;

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

        getMovieId();
        getViews(view);
        //TODO setupRelatedMoviesListView(), setupPaginationForRelatedMovies()

        mPresenter = new MovieDetailPresenter(this);
        mPresenter.getMovieDetail(mMovieId);
    }

    private void getMovieId() {
        Bundle args = getArguments();
        mMovieId = ((args != null) ? args.getInt("movieId") : -1);
    }

    private void getViews(View view) {
        mOriginalTitleView = view.findViewById(R.id.tv_original_title);
        mOriginalLanguageView = view.findViewById(R.id.tv_original_language);
        mVoteAverageView = view.findViewById(R.id.tv_vote_avg);
        mRuntimeView = view.findViewById(R.id.tv_runtime);
        mPosterView = view.findViewById(R.id.iv_movie_poster);
        mOverviewView = view.findViewById(R.id.tv_overview);
        mRelatedMoviesListView = view.findViewById(R.id.rv_related_movies_list);
        mDetailsLoadingBar = view.findViewById(R.id.pb_details_loading);
        mDetailsErrorView = view.findViewById(R.id.tv_details_error);
        mDetailsWrapper = view.findViewById(R.id.ll_details_wrapper);
    }

    @Override
    public void onMovieDetailLoaded(MovieModel movie) {
        mDetailsLoadingBar.setVisibility(View.GONE);
        mDetailsErrorView.setVisibility(View.GONE);
        mDetailsWrapper.setVisibility(View.VISIBLE);

        Glide.with(this)
                .load(MovieDbApiService.IMAGE_BASE_URL + movie.poster_path)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.default_poster)
                        .error(R.drawable.default_poster))
                .into(mPosterView);

        mOriginalTitleView.setText(movie.original_title);
        mOriginalLanguageView.setText(movie.original_language);
        mVoteAverageView.setText(String.valueOf(movie.vote_average));
        mOverviewView.setText(movie.overview);

        if (movie.runtime != null) {
            mRuntimeView.setText(String.format(Locale.getDefault(), getString(R.string.runtime_minutes), movie.runtime));
        } else {
            mRuntimeView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onError(Throwable error) {
        Log.e(TAG, "onError: error", error);
    }
}
