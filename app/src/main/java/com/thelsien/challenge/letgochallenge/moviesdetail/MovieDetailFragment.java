package com.thelsien.challenge.letgochallenge.moviesdetail;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.models.MovieDetailModel;
import com.thelsien.challenge.letgochallenge.models.MovieListModel;
import com.thelsien.challenge.letgochallenge.models.MovieRowModel;

import java.util.Locale;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View, SimilarMoviesAdapter.OnSimilarMovieClickedListener {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
    private static final int SIMILAR_MOVIES_VISIBLE_TRESHOLD = 2;

    private MovieDetailContract.Presenter mPresenter;
    private MovieDetailModel mMovieWithDetails;
//    private MovieRowModel mMovieFromList;

    private int mMovieId;

    private ImageView mPosterView;
    private TextView mOriginalTitleView;
    private TextView mOriginalLanguageView;
    private TextView mVoteAverageView;
    private TextView mRuntimeView;
    private TextView mOverviewView;
    private RecyclerView mSimilarMoviesListView;
    private ProgressBar mDetailsLoadingBar;
    private TextView mDetailsErrorView;
    private LinearLayout mDetailsWrapper;

    private LinearLayoutManager mHorizontalLayoutManager;
    private int mPage = 1;
    private MovieListModel mMovieListModel;
    private boolean isLoading = false;

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
        View view = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        mPosterView = view.findViewById(R.id.iv_movie_poster);
        mPosterView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                mPosterView.getViewTreeObserver().removeOnPreDrawListener(this);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().supportStartPostponedEnterTransition();
                }
                return true;
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getMovieId();
        getViews(view);
        setupRelatedMoviesListView();
        setupPaginationForRelatedMovies();

        mPresenter = new MovieDetailPresenter(this);
        mPresenter.getMovieDetail(mMovieId);
    }

    private void setupPaginationForRelatedMovies() {
        mSimilarMoviesListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = mHorizontalLayoutManager.getItemCount();
                int lastVisibleItem = mHorizontalLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && mPage <= mMovieListModel.total_pages && totalItemCount <= (lastVisibleItem + SIMILAR_MOVIES_VISIBLE_TRESHOLD)) {
                    mPage++;
                    mPresenter.getSimilarMovies(mMovieId, mPage);
                    isLoading = true;
                }
            }
        });
    }

    private void setupRelatedMoviesListView() {
        mHorizontalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mSimilarMoviesListView.setLayoutManager(mHorizontalLayoutManager);
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
        mSimilarMoviesListView = view.findViewById(R.id.rv_similar_movies_list);
        mDetailsLoadingBar = view.findViewById(R.id.pb_details_loading);
        mDetailsErrorView = view.findViewById(R.id.tv_details_error);
        mDetailsWrapper = view.findViewById(R.id.ll_details_wrapper);
    }

    @Override
    public void onMovieDetailLoaded(MovieDetailModel movie) {
        mDetailsLoadingBar.setVisibility(View.GONE);
        mDetailsErrorView.setVisibility(View.GONE);
        mDetailsWrapper.setVisibility(View.VISIBLE);

        mMovieWithDetails = movie;

//        Glide.with(this)
//                .load(MovieDbApiService.IMAGE_BASE_URL + movie.poster_path)
//                .apply(new RequestOptions()
//                        .placeholder(R.drawable.default_poster)
//                        .error(R.drawable.default_poster))
//                .into(mPosterView);

        mOriginalTitleView.setText(movie.original_title);
        mOriginalLanguageView.setText(movie.original_language);
        mVoteAverageView.setText(String.valueOf(movie.vote_average));
        mOverviewView.setText(movie.overview);

        if (movie.runtime != null) {
            mRuntimeView.setText(String.format(Locale.getDefault(), getString(R.string.runtime_minutes), movie.runtime));
        } else {
            mRuntimeView.setVisibility(View.GONE);
        }

        //start loading similar movies
        mPresenter.getSimilarMovies(mMovieId, mPage);
    }

    @Override
    public void onSimilarMoviesLoaded(MovieListModel movieListModel) {
        mMovieListModel = movieListModel;

        SimilarMoviesAdapter adapter = (SimilarMoviesAdapter) mSimilarMoviesListView.getAdapter();
        if (adapter == null) {
            adapter = new SimilarMoviesAdapter(this);
            adapter.addCurrentMovie(mMovieWithDetails);
            mSimilarMoviesListView.setAdapter(adapter);
        }

        adapter.addMovies(mMovieListModel.results);
//        mSimilarMoviesListView.setAdapter(adapter);

        isLoading = false;
    }

    @Override
    public void onError(Throwable error) {
        Log.e(TAG, "onError: error", error);
    }

    @Override
    public void onSimilarMovieClicked(MovieRowModel movie) {
        Intent detailIntent = new Intent(getContext(), MovieDetailActivity.class);
        detailIntent.putExtra("movieId", movie.id);

        startActivity(detailIntent);
    }
}
