package com.thelsien.challenge.letgochallenge.util;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.thelsien.challenge.letgochallenge.R;
import com.thelsien.challenge.letgochallenge.api.MovieDbApiService;

public class GlideHelper {
    public static RequestBuilder<Drawable> getGlideRequest(Context context, String url) {
        return Glide.with(context)
                .load(MovieDbApiService.IMAGE_BASE_URL + url)
                .apply(new RequestOptions()
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .placeholder(R.drawable.default_poster)
                        .error(R.drawable.default_poster));
    }
}
