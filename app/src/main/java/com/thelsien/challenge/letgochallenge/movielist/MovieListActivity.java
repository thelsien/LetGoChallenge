package com.thelsien.challenge.letgochallenge.movielist;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.thelsien.challenge.letgochallenge.R;

public class MovieListActivity extends AppCompatActivity {

    private final String MOVIE_LIST_FRAGMENT_TAG = "movie_list_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fl_fragment_container, new MovieListFragment(), MOVIE_LIST_FRAGMENT_TAG)
                .commit();
    }
}
