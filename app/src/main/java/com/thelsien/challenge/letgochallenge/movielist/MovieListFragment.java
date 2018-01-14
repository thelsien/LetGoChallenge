package com.thelsien.challenge.letgochallenge.movielist;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.models.MovieListResultModel;
import com.thelsien.challenge.letgochallenge.models.TopRatedModel;
import com.thelsien.challenge.letgochallenge.moviesdetail.MovieDetailActivity;

public class MovieListFragment extends Fragment implements MovieListContract.View, MovieListAdapter.OnMovieClickListener {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private final static int VISIBLE_THRESHOLD = 2;

    private TopRatedModel mTopRatedModel;
    private MovieListContract.Presenter mPresenter;
    private int mPage = 1; //TODO savedInstanceState?

    private TextView mErrorTextView;
    private ProgressBar mLoadingBar;
    private RecyclerView mListView;
    private GridLayoutManager mLayoutManager;
    private boolean isLoading = true;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViews(view);

        setupRecyclerView();
        setupPagination();

        mPresenter = new MovieListPresenter(this);
        mPresenter.getTopRatedMovies(mPage);
    }

    private void getViews(@NonNull View view) {
        mListView = view.findViewById(R.id.rv_list);
        mLoadingBar = view.findViewById(R.id.pb_loading);
        mErrorTextView = view.findViewById(R.id.tv_error);
    }

    private void setupRecyclerView() {
        mLayoutManager = new GridLayoutManager(getContext(), 2);
        mListView.setLayoutManager(mLayoutManager);
    }

    private void setupPagination() {
        mListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int totalItemCount = mLayoutManager.getItemCount();
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && mPage <= mTopRatedModel.total_pages && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    Log.d(TAG, "onScrolled: called" + totalItemCount + " " + lastVisibleItem);
                    mPage++;
                    mPresenter.getTopRatedMovies(mPage);
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public void onTopRatedMoviesLoaded(TopRatedModel topRatedModel) {
        mTopRatedModel = topRatedModel;
        MovieListAdapter adapter = (MovieListAdapter) mListView.getAdapter();
        if (adapter == null) {
            showList();
            adapter = new MovieListAdapter(this);
            mListView.setAdapter(adapter);
        }

        adapter.addMovies(topRatedModel.results);

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

        //TODO check for missing api key and no network errors, otherwise show unknown error text

        mErrorTextView.setText(R.string.movie_list_unknown_error);
    }

    @Override
    public void onMovieClicked(MovieListResultModel movie) {
        Intent detailIntent = new Intent(getContext(), MovieDetailActivity.class);
        detailIntent.putExtra("movieId", movie.id);

        startActivity(detailIntent);
    }
}
