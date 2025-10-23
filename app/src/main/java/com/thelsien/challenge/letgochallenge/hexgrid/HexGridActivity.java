package com.thelsien.challenge.letgochallenge.hexgrid;

import android.os.Bundle;

import com.thelsien.challenge.letgochallenge.BaseActivty;
import com.thelsien.challenge.letgochallenge.R;

/**
 * Activity that displays a hexagonal grid filling the entire screen
 */
public class HexGridActivity extends BaseActivty {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex_grid);
    }
}
