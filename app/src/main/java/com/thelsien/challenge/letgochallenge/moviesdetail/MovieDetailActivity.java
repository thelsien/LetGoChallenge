package com.thelsien.challenge.letgochallenge.moviesdetail;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.thelsien.challenge.letgochallenge.R;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String DETAIL_FRAGMENT_TAG = "detail_fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detial);

        supportPostponeEnterTransition();

        Intent intent = getIntent();
        MovieDetailFragment detailFragment = MovieDetailFragment.createNewInstance(intent.getIntExtra("movieId", -1));

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fl_fragment_container, detailFragment, DETAIL_FRAGMENT_TAG)
                .commit();
    }
}
