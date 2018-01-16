package com.thelsien.challenge.letgochallenge.movielist;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thelsien.challenge.letgochallenge.BaseActivty;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.models.MovieListModel;
import com.thelsien.challenge.letgochallenge.models.MovieRowModel;
import com.thelsien.challenge.letgochallenge.moviesdetail.MovieDetailActivity;

import retrofit2.HttpException;

public class MovieListActivity extends BaseActivty implements MovieListContract.View, MovieListAdapter.OnMovieClickListener {

    private final static int VISIBLE_MOVIE_THRESHOLD = 4;

    private MovieListModel mMovieListModel;
    private MovieListContract.Presenter mPresenter;
    private int mPage = 1;

    private TextView mErrorTextView;
    private ProgressBar mLoadingBar;
    private RecyclerView mListView;
    private GridLayoutManager mLayoutManager;
    private boolean isLoading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getViews();
        setupRecyclerView();
        setupPagination();

        mPresenter = new MovieListPresenter(this);
        mPresenter.getTopRatedMovies(mPage);
    }

    private void getViews() {
        mListView = findViewById(R.id.rv_list);
        mLoadingBar = findViewById(R.id.pb_loading);
        mErrorTextView = findViewById(R.id.tv_error);
    }

    private void setupRecyclerView() {
        mLayoutManager = new GridLayoutManager(this, 2);
        mListView.setLayoutManager(mLayoutManager);
    }

    private void setupPagination() {
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && mPage <= mMovieListModel.total_pages && totalItemCount <= (lastVisibleItem + VISIBLE_MOVIE_THRESHOLD)) {
                    mPage++;
                    mPresenter.getTopRatedMovies(mPage);
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public void onTopRatedMoviesLoaded(MovieListModel movieListModel) {
        mMovieListModel = movieListModel;
        MovieListAdapter adapter = (MovieListAdapter) mListView.getAdapter();
        if (adapter == null) {
            showList();
            adapter = new MovieListAdapter(this);
            mListView.setAdapter(adapter);
        }

        adapter.addMovies(movieListModel.results);

        isLoading = false;
    }

    private void showList() {
        mListView.setVisibility(View.VISIBLE);
        mErrorTextView.setVisibility(View.GONE);
        mLoadingBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        mLoadingBar.setVisibility(View.GONE);
        mListView.setVisibility(View.GONE);
        mErrorTextView.setVisibility(View.VISIBLE);

        if (error instanceof HttpException && ((HttpException) error).code() == 401) {
            mErrorTextView.setText(R.string.movie_list_api_key_error);
        } else {
            mErrorTextView.setText(R.string.movie_list_unknown_error);
        }
    }

    @Override
    public void onMovieClicked(MovieRowModel movie, View sharedElement) {
        Intent detailIntent = new Intent(this, MovieDetailActivity.class);
        detailIntent.putExtra("movie", movie);
        detailIntent.putExtra("poster_transition_name", ViewCompat.getTransitionName(sharedElement));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityCompat.startActivity(
                    this,
                    detailIntent,
                    ActivityOptions.makeSceneTransitionAnimation(
                            this,
                            sharedElement,
                            ViewCompat.getTransitionName(sharedElement)
                    ).toBundle()
            );
        } else {
            startActivity(detailIntent);
        }
    }
}
