package com.thelsien.challenge.letgochallenge.moviesdetail;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;
import com.thelsien.challenge.letgochallenge.models.MovieDetailModel;
import com.thelsien.challenge.letgochallenge.models.MovieListModel;
import com.thelsien.challenge.letgochallenge.models.MovieRowModel;

import java.util.Locale;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View, SimilarMoviesAdapter.OnSimilarMovieClickedListener {
    private static final int SIMILAR_MOVIES_VISIBLE_TRESHOLD = 2;
    private static final String TAG = MovieDetailActivity.class.getSimpleName();

    private MovieDetailContract.Presenter mPresenter;
    private MovieDetailModel mMovieWithDetails;
//    private MovieRowModel mMovieFromList;

    private int mMovieId;
    private String mPosterPath;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detial);

        supportPostponeEnterTransition();

        getViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = getIntent().getStringExtra("poster_transition_name");
            ViewCompat.setTransitionName(mPosterView, imageTransitionName);
        }

        mMovieId = getIntent().getIntExtra("movieId", -1);
        mPosterPath = getIntent().getStringExtra("poster_path");

        Glide.with(this)
                .load(MovieDbApiService.IMAGE_BASE_URL + mPosterPath)
                .apply(new RequestOptions()
                        .dontTransform()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.default_poster)
                        .error(R.drawable.default_poster))
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        mPosterView.setImageDrawable(resource);
                        supportStartPostponedEnterTransition();
                    }
                });

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
        mHorizontalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mSimilarMoviesListView.setLayoutManager(mHorizontalLayoutManager);
    }

    private void getViews() {
        mOriginalTitleView = findViewById(R.id.tv_original_title);
        mOriginalLanguageView = findViewById(R.id.tv_original_language);
        mVoteAverageView = findViewById(R.id.tv_vote_avg);
        mRuntimeView = findViewById(R.id.tv_runtime);
        mPosterView = findViewById(R.id.iv_movie_poster);
        mOverviewView = findViewById(R.id.tv_overview);
        mSimilarMoviesListView = findViewById(R.id.rv_similar_movies_list);
        mDetailsLoadingBar = findViewById(R.id.pb_details_loading);
        mDetailsErrorView = findViewById(R.id.tv_details_error);
        mDetailsWrapper = findViewById(R.id.ll_details_wrapper);
    }

    @Override
    public void onMovieDetailLoaded(MovieDetailModel movie) {
        mDetailsLoadingBar.setVisibility(View.GONE);
        mDetailsErrorView.setVisibility(View.GONE);
        mDetailsWrapper.setVisibility(View.VISIBLE);

        mMovieWithDetails = movie;

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
    public void onSimilarMovieClicked(MovieRowModel movie, View sharedView) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra("movieId", movie.id);
        detailIntent.putExtra("poster_path", movie.poster_path);
        detailIntent.putExtra("poster_transition_name", ViewCompat.getTransitionName(sharedView));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            startActivity(
                    detailIntent,
                    ActivityOptions.makeSceneTransitionAnimation(
                            this,
                            sharedView,
                            ViewCompat.getTransitionName(sharedView)
                    ).toBundle()
            );
        } else {
            startActivity(detailIntent);
        }
    }
}
